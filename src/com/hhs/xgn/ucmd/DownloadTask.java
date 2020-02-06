package com.hhs.xgn.ucmd;


public class DownloadTask {
	public String url;
	public String filesize;
	public String name;
	public DownloadTask(){
		
	}
//	public DownloadTask(CurseFile cf) throws Exception{
//		url=cf.downloadURL()
//		filesize=cf.fileSize();
//		name=cf.name();
//	}
	@Override
	public String toString() {
		return "DownloadTask [url=" + url + ", filesize=" + filesize + ", name=" + name + "]";
	}
	
	
}
