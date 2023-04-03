package by.katz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main implements Timer.ITimer {

    public static final Settings SETTINGS = Settings.getInstance();

    public Main() {
        var timer = new Timer(this, SETTINGS.getTimeUnit(), SETTINGS.getTimeValue());
        timer.start();
    }

    public static void main(String[] args) {new Main();}

    @Override public void onTimerEvent() {
        var sdf = new SimpleDateFormat("dd-MM-yyyy___HH_mm_ss");
        var time = sdf.format(new Date());
        var backupDir = new File(SETTINGS.getBackupDir());
        if (!backupDir.exists() && !backupDir.mkdirs()) {
            System.err.println("bad dir: " + backupDir);
            return;
        }
        ZipUtils.zipDirectory(new File(SETTINGS.getTargetDir()), new File(backupDir, time + ".zip"));
    }
}