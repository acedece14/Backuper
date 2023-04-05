package by.katz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class Settings {

    private static final File FILE_SETTINGS = new File("backuper.json");
    private static Settings instance;

    private String backupDir = "backup";
    private String targetDir = "c:\\Config.Msi ";
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    private Integer timeValue = 10;
    private String exeName = "calc.exe";

    private Settings() {}

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
            instance.load();
        }
        return instance;
    }

    public void load() {
        if (!FILE_SETTINGS.exists())
            saveSettings();
        try {
            instance = new Gson().fromJson(Files.readString(FILE_SETTINGS.toPath()), Settings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Loaded: " + instance);
    }

    public void saveSettings() {
        var json = new GsonBuilder()
              .setPrettyPrinting()
              .create()
              .toJson(this);
        try {
            Files.writeString(FILE_SETTINGS.toPath(), json);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public Integer getTimeValue() {return timeValue;}

    public TimeUnit getTimeUnit() {return timeUnit;}

    public String getBackupDir() {return backupDir;}

    public String getTargetDir() {return targetDir;}

    public String getExeName() {return exeName;}
}
