package com.badals.admin.domain.pojo;

import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Data
public class Gallery implements Serializable {
   private static final long serialVersionUID = -5573648649422261369L;
   public Gallery(String url) {
      this.url = url;
   }

   public Gallery() {
   }

   String url;

   private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
   {
      url = aInputStream.readUTF();
   }

   private void writeObject(ObjectOutputStream aOutputStream) throws IOException
   {
      aOutputStream.writeUTF(url);
   }
}
