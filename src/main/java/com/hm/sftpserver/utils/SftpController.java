package com.hm.sftpserver.utils;

import com.jcraft.jsch.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/Api")
public class SftpController {

    @PostMapping("/retrieve-file")
    public String readFromSftp(@RequestBody String fileName)  {
        String host="";
        int port=22;
        String userName="root";
        String password="Test@123";
        String sftpFilePath="" +fileName;
        StringBuilder fileContent=new StringBuilder();

try {
    JSch jsch = new JSch();
    Session session = jsch.getSession(userName, host, port);
    session.setPassword(password);
    session.setConfig("StrictHostKeyChecking", "no");
    session.connect();
    Channel channel=session.openChannel("sftp");
    channel.connect();
    ChannelSftp sftpChannel= (ChannelSftp) channel;
    InputStream inputStream=sftpChannel.get(sftpFilePath);
    BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
    String line;
    while((line=reader.readLine()) != null){
        fileContent.append(line).append("\n");
    }
    reader.close();
    sftpChannel.disconnect();
    session.disconnect();
}catch (JSchException | SftpException | IOException e){
        e.printStackTrace();
return "Error occured while reading from sftp location. ";
}
return "File content for ' "+ fileName+ "'from SFTP location: \n'"+fileContent.toString();
    }
}
