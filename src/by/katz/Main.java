package by.katz;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main implements Timer.ITimer {

    public static final Settings SETTINGS = Settings.getInstance();

    public Main() {
        System.out.println("Start app");
        new Timer(this, SETTINGS.getTimeUnit(), SETTINGS.getTimeValue()).start();
    }

    public static void main(String[] args) {new Main();}

    @Override public void onTimerEvent() {
        if (!processIsRunning(SETTINGS.getExeName())) {
            System.out.println(SETTINGS.getExeName() + " not running");
            return;
        }
            var time = new SimpleDateFormat("dd-MM-yyyy___HH_mm_ss").format(new Date());
        var backupDir = new File(SETTINGS.getBackupDir());
        if (!backupDir.exists() && !backupDir.mkdirs()) {
            System.err.println("bad dir: " + backupDir);
            return;
        }
        ZipUtils.zipDirectory(new File(SETTINGS.getTargetDir()), new File(backupDir, time + ".zip"));
    }

    private boolean processIsRunning(String procName) {
        var filenameFilter = "/nh /fi \"Imagename eq " + procName + "\"";
        var tasksCmd = System.getenv("windir") + "/system32/tasklist.exe " + filenameFilter;
        Process process;
        try {
            process = Runtime.getRuntime().exec(tasksCmd);
        } catch (IOException e) {throw new RuntimeException(e);}
        var processes = new ArrayList<String>();
        try (var input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            for (var line = ""; (line = input.readLine()) != null; )
                processes.add(line);
        } catch (IOException e) {throw new RuntimeException(e);}
        return processes.stream().anyMatch(row -> row.toLowerCase().startsWith(procName.toLowerCase()));
    }
}
