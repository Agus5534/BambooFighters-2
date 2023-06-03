package io.github.agus5534.bamboofightersv2.utils.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileCreator {
    private final String name;
    private final File file;

    private List<FileCreator> parentFiles;

    public FileCreator(File path, String name) {
        this.name = name;
        file = new File(path + "/" + name);
        parentFiles = new ArrayList<>();

        try {
            this.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException {
        if(!file.exists()) {
            if(this.name.endsWith("/")) {
                file.mkdir();
                return;
            }

            file.createNewFile();
        }
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public List<File> getContents() {
        var l = new ArrayList<File>();

        Arrays.stream(this.file.listFiles()).forEach(f -> l.add(f));

        return l;
    }

    public List<File> getContents(String extension) {
        var l = new ArrayList<File>();

        Arrays.stream(this.file.listFiles()).filter(f -> f.getName().endsWith(extension)).forEach(f -> l.add(f));

        return l;
    }

    public boolean hasFile(String name) {
        boolean f = false;

        for (var file : getContents()) {
            if(file.getName().equalsIgnoreCase(name)) {
                f = true;
            }
        }

        return f;
    }

    public File getFile(String name) {
        File f = null;

        for (var file : getContents()) {
            if(file.getName().equalsIgnoreCase(name)) {
                f = file;
            }
        }

        return f;
    }

    public boolean isExtension(String extension) {
        return file.getName().endsWith(extension);
    }

    public void registerParentFiles(FileCreator... fileCreator) {
        Arrays.stream(fileCreator).forEach(parentFiles::add);
    }

    public List<FileCreator> getParentFiles() {
        return parentFiles;
    }
}
