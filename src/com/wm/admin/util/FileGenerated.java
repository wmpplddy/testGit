package com.wm.admin.util;

import com.wm.admin.domain.User;
import com.wm.admin.service.UserService;
import org.mvc.MvcFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileGenerated {
    private static String UserFilePath = "C:/his";

    public static void GenerateUserInfo() {
        File f = new File(UserFilePath);
        System.out.println("File操作中：" + f.exists());
        if (f.exists()) {
            f.delete();
            f.mkdir();
        } else {
            f.mkdir();
        }
        UserService serv = MySpring.getBean(UserService.class);
        List<String> zjms = serv.allZJM();
        for (String zjm : zjms) {
            generateUser(zjm);
        }
    }

    private static void generateUser(String u_zjm) {//生成文件夹
        String u_path = UserFilePath + "/" + u_zjm;
        File f = new File(u_path);
        f.mkdir();
    }

    /**
     * 上传用户头像 ，参数需要 用户zjm 以及 头像文件
     */
    public static boolean UserAvatarADD(String u_zjm, MvcFile file) {
        if(u_zjm.equals("")){return false;}
        String u_path = UserFilePath + "/" + u_zjm;
        File userFile = new File(u_path);
        if (userFile.exists() && file!=null) {
            String fix = file.getContentType().split("/")[1];
            String av_path = u_path + "/avatar." + fix;
            File ava = new File(av_path);
            boolean isWrite = writeBytes(ava,file.getContent());
            if(isWrite){
                Map<String, String> m = new HashMap<>();
                m.put("u_zjm", u_zjm);
                m.put("path", ava.getAbsolutePath());
                UserService serv = MySpring.getBean(UserService.class);
                serv.updateUserAvaPath(m);
                return true;
            }
            return false;
        }else{
            userFile.mkdir();
            return true;
        }
    }
    private static byte[] getFileBytes(File f){
        FileInputStream fos = null;
        try {
            fos = new FileInputStream(f);
            byte[] bs = fos.readAllBytes();
            return bs;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private static boolean writeBytes(File f,byte[] bytes){
        FileOutputStream os = null;
        //如果不存在老头像
        try {
            os = new FileOutputStream(f);
            if (!f.exists()) {
                f.createNewFile();
                os.write(bytes);
            } else {
                f.delete();
                f.createNewFile();
                os.write(bytes);
            }
            os.flush();
            os.close();
            //写入数据库用户数据 yl1字段
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static byte[] GetUserAvatar(User user) {
        String path = user.getYl1();
        if (path == null || "".equals(path)) return null;
        File f = new File(path);
        if(!f.exists()){return null;}
        return getFileBytes(f);
    }

    public static void deleteUserFile(String zjm){
        String u_path = UserFilePath + "/" + zjm;
        File f = new File(u_path);
        if(f.exists()){
            f.delete();
        }
    }

}
