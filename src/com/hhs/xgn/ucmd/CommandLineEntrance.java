package com.hhs.xgn.ucmd;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class CommandLineEntrance {
	public static void main(String[] args) throws JsonSyntaxException, JsonIOException, UnsupportedEncodingException, FileNotFoundException {
		CommandLineEntrance cml=new CommandLineEntrance();
		cml.solve(args);
	}
	
	ModpackManifest mm;
	Gson gs;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    
    String read(String url) throws MalformedURLException, IOException{
    	InputStream is=new URL(url).openConnection().getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is, "utf-8"));
		String s="",sl="";
		while((sl=br.readLine())!=null){
			s+=sl;
		}
		br.close();
		return s;
    }
    
	void solve(String[] args) throws JsonSyntaxException, JsonIOException, UnsupportedEncodingException, FileNotFoundException{
		if(args.length<2){
			System.out.println("usage: <manifest.json> <out file>");
			System.exit(1);
		}
		
		gs=new Gson();
		mm=gs.fromJson(new InputStreamReader(new FileInputStream(args[0]), "utf-8"), ModpackManifest.class);
		
		System.out.println("Modpack Information:");
		System.out.println("Pack="+mm.name+" by "+mm.author);
		System.out.println("Manifest="+mm.manifestType+" of v"+mm.manifestVersion);
		System.out.println("Override="+mm.overrides);
		System.out.println();
		System.out.println("Minecraft Information:");
		System.out.println("MC Version="+mm.minecraft.version);
		System.out.println("Modloader="+mm.minecraft.modLoaders);
		
		PrintWriter pw=new PrintWriter(args[1]);
		
		int i=0;
		for(ModpackFile mf:mm.files){
			try{
				i++;
				
				System.out.println();
				System.out.println("***"+i+"/"+mm.files.size()+"***");
				
				System.out.println("Getting download link for "+mf.projectID+" "+mf.fileID);
				
				String url="https://ddph1n5l22.execute-api.eu-central-1.amazonaws.com/dev/mod/"+mf.projectID;
				
				String s=read(url);
				
				JsonObject jo=gs.fromJson(s, JsonObject.class);
				String key=jo.get("result").getAsJsonObject().get("key").getAsString();
				
				System.out.println("Find project key="+key);
				
				String url2="https://ddph1n5l22.execute-api.eu-central-1.amazonaws.com/dev/mod/"+key+"/files";
				
				s=read(url2);
				
				jo=gs.fromJson(s, JsonObject.class);
				JsonArray ja=jo.get("result").getAsJsonArray();
				String furl=null;
				for(int j=0;j<ja.size();j++){
					JsonObject file=ja.get(j).getAsJsonObject();
					if(file.get("id").getAsInt()==mf.fileID){
						String name=file.get("file_name").getAsString();
						System.out.println("Find name:"+name);
						furl="https://media.forgecdn.net/files/"+mf.fileID/1000+"/"+mf.fileID%1000+"/"+name;
						System.out.println("So, furl="+furl);
						break;
					}
				}
				pw.println(furl);
				pw.flush();
			}catch(Exception e){
				System.out.println("Failed!"+e);
				e.printStackTrace();
			}
		}
		
		System.out.println("Set!");
		pw.close();
	}
}
