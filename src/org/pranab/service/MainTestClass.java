package org.pranab.service;

import org.json.simple.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainTestClass {
    public static void main(String[] args) {
        List files;
        ZipUtils zipUtils;
        try {
            zipUtils = new ZipUtils();
            File zipFile = new File("F:\\demo.zip");
            String destDirectory = "D:\\unzip" + zipUtils.removeExtension(zipFile.getName());
            JSONObject response = zipUtils.unzip(zipFile, destDirectory);
            files = (ArrayList)response.get("files");
            files.stream().forEach(x -> {
                String fileType = zipUtils.getExtension(x.toString()).get();
                if (fileType.equalsIgnoreCase("bpmn2")) {
                    System.out.println("this is bpmn2 file");
                }
                if (fileType.equalsIgnoreCase("js") || fileType.equalsIgnoreCase("py")){
                    System.out.println("this is script file");
                }
                if (fileType.equalsIgnoreCase("jar") || fileType.equalsIgnoreCase("json")){
                    System.out.println("this is widget file");
                }
            });
            String directory = response.get("destDirectory").toString();
            File destFile = new File(directory);
            JSONObject lookForFiles = zipUtils.endWith(directory, "-info.json");
            System.out.println(lookForFiles);
            deleteDirectory(destFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean deleteDirectory(File directory) {
        if(directory.exists()){
            File[] files = directory.listFiles();
            if(files != null){
                for(int i=0; i<files.length; i++) {
                    if(files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return(directory.delete());
    }

    public int add(int a, int b){
        return a+b;
    }
}
