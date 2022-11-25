package com.yun.tech.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Test java serializable interface
 * First, store serialized data in disk
 * And, modify some variable in original class
 * And, deserialize the data storing in disk.
 */
public class ObjectSerializableTest{
    /**
     * Serialize class and place to local disk.
     *
     * @param m class parameter to be serialized
     * @param filePath a location of being stored in disk
     * @throws IOException when can't find file by filePath
     */
    private static void serializeClassToDisk(Member m, String filePath) throws IOException {
        byte[] serialized;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(m);
                serialized = baos.toByteArray();
            }
        }
        // base64로 인코딩한 문자열
        String base64Member = Base64.getEncoder().encodeToString(serialized);

        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(base64Member);
        writer.close();
    }

    /**
     * Deserialzie class data from Disk.
     *
     * @param filePath a location of class data deserialized
     */
    private static void deserialzeClassFromDisk(String filePath) throws IOException, ClassNotFoundException {
        byte[] base64Member = null;
        int nBytesToRead = 0;
        try (FileInputStream f = new FileInputStream(filePath)) {
            try (DataInputStream reader = new DataInputStream(f)) {
                nBytesToRead = reader.available();

                base64Member = new byte[nBytesToRead];
                if (nBytesToRead > 0) {
                    reader.read(base64Member);
                }
            }
        }

        byte[] deserialized = Base64.getDecoder().decode(base64Member);

        try(ByteArrayInputStream bais = new ByteArrayInputStream(deserialized)) {
            try(ObjectInputStream ois = new ObjectInputStream(bais)) {
                Object objectMember = ois.readObject();
                // member 객체로 역직렬화
                Member readMember = (Member) objectMember;
                System.out.println(readMember);
            }
        }

    }

    public static void main(String[] args) throws Exception {
       // Member member = new Member("SY", "seongukyun17@gmail.com", 30, "?");

        //serializeClassToDisk(member, "C:\\Users\\seonguk.yun\\Downloads\\serialized1");

        deserialzeClassFromDisk("C:\\Users\\seonguk.yun\\Downloads\\serialized1");

    }


}
