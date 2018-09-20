package com.hstypay.sandbox.support.type;

/**
 * 文件路径信息
 * @author Tinnfy Lee
 *
 */
public class FilePathInfo {

    private static final String FOLDER_SEPARATOR = "/";
    private static final char EXTENSION_SEPARATOR = '.';

    //文件路径
    private String path;
    //不带后缀的文件名
    private String fileName;
    //文件后缀
    private String fileExtension;

    public FilePathInfo(String filePathName) {
        tripFilePathInfo(filePathName);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * 萃取分割文件信彷
     * @param fullPath
     */
    private void tripFilePathInfo(String fullPath) {
        if (fullPath == null) {
            return;
        }

        int folderIndex = fullPath.lastIndexOf(FOLDER_SEPARATOR);
        if (folderIndex > 0) {
            path = fullPath.substring(0, folderIndex);
        }

        int extIndex = fullPath.lastIndexOf(EXTENSION_SEPARATOR);
        if (extIndex >0 && folderIndex < extIndex) {
            fileExtension = fullPath.substring(extIndex + 1);
            fileName = fullPath.substring(folderIndex+1, extIndex);
        } else {
            fileName = fullPath.substring(folderIndex+1);
        }
    }
}
