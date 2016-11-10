package ru.universum.Client;
import java.io.File;
import java.net.URI;

class Util
{
    private static File workDir = null;

    static File getWorkingDirectory() {
        if (workDir == null) workDir = getWorkingDirectory("NEOnline");
        return workDir;
    }

    private static File getWorkingDirectory(String applicationName) {
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;
        switch (getPlatform().ordinal()) {
            case 0:
            case 1:
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case 2:
                String applicationData = System.getenv("APPDATA");
                if (applicationData != null)
                    workingDirectory = new File(applicationData, "." + applicationName + '/');
                else
                    workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
            case 3:
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
            default:
                workingDirectory = new File(userHome, applicationName + '/');
        }
        if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs())) throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        return workingDirectory;
    }

    private static OS getPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) return OS.windows;
        if (osName.contains("mac")) return OS.macos;
        if (osName.contains("solaris")) return OS.solaris;
        if (osName.contains("sunos")) return OS.solaris;
        if (osName.contains("linux")) return OS.linux;
        if (osName.contains("unix")) return OS.linux;
        return OS.unknown;
    }

    static void openLink(URI uri) {
        try {
            Object o = Class.forName("java.awt.Desktop").getMethod("getDesktop", new Class[0]).invoke(null);
            o.getClass().getMethod("browse", new Class[] { URI.class }).invoke(o, uri);
        } catch (Throwable e) {
            System.out.println("Failed to open link " + uri.toString());
        }
    }

    private enum OS
    {
        linux, solaris, windows, macos, unknown
    }
}