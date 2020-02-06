package com.hhs.xgn.ucmd;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
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
				
				InputStream is=new URL(url).openConnection().getInputStream();
				BufferedReader br=new BufferedReader(new InputStreamReader(is, "utf-8"));
				String s="",sl="";
				while((sl=br.readLine())!=null){
					s+=sl;
				}
				br.close();
				
				
//				System.out.println("API Result String="+s);
				//parse json
				JsonObject jo=gs.fromJson(s, JsonObject.class);
				String url2=jo.get("result").getAsJsonObject().get("url").getAsString();
				
				System.out.println("Find project url="+url2);
				String furl=url2+"/download/"+mf.fileID+"/file";
				System.out.println("So file url="+furl);
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
