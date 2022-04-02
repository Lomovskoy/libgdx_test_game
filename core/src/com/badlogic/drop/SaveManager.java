package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class SaveManager {

    private final FileHandle file;

    public SaveManager() {
        file = Gdx.files.local("assets" + File.separator + "save.txt");
    }

    public String getSave() {
        return file.readString();
    }

    public void setRecord(int dropsGathered) {
        int record = Integer.parseInt(file.readString());
        if (dropsGathered > record) {
            file.writeString(String.valueOf(dropsGathered), false);
        }
    }
}
