package com.hasegawa.hahaseshin17;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Xml;
import android.widget.TextView;




public class MainActivity extends Activity {
private TextView mView;

//article
static private String mArticleTitle[];
static private String mArticleURL[];
static private int mArticleNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		mView=(TextView)findViewById(R.id.view);
		mView.setText(new String(httpGet(createURL())));
	}

	public String createURL(){
		String apiURL="http://news.yahooapis.jp/NewsWebService/V2/topics?";
		String appid="dj0zaiZpPW15bXdMdThNa0FMcSZzPWNvbnN1bWVyc2VjcmV0Jng9NmU-";
		String category="top";
		return String.format("%sappid=%s&pickupcategory=%s", apiURL,appid,category);
	}

	public static String httpGet(String strURL){
		//(1)トライキャッチによるエラー処理
try{
    //(2)URLクラスを使って通信
URL url=new URL(strURL);
URLConnection connection=url.openConnection();
//(3)動作を入力に設定
connection.setDoInput(true); //データを入力することの宣言
InputStream stream=connection.getInputStream();
readXML(stream);
String data="";
for(int i=0;i<mArticleNum;i++){
	data+=mArticleTitle[i];
}
//(5)終了処理
stream.close();
return data;
}catch(Exception e){

	//(6)エラー処理
   return e.toString();
}
	}

public static void readXML(InputStream stream)
throws XmlPullParserException{
	try{
		XmlPullParser myxmlPullParser=Xml.newPullParser();
		myxmlPullParser.setInput(stream,"UTF-8");

		int cntTitle=0;
		int cntAddress=0;
		for (int e =myxmlPullParser.getEventType(); e!=XmlPullParser.END_DOCUMENT;e=myxmlPullParser.next()){
			if(e==XmlPullParser.START_TAG){
				if(myxmlPullParser.getName().equals("ResultSet")){
					mArticleNum=Integer.parseInt(myxmlPullParser.getAttributeValue(null,"totalResultsReturned"));
					mArticleTitle=new String[mArticleNum];
					mArticleURL=new String[mArticleNum];
				}else if(myxmlPullParser.getName().equals("Title")){
					mArticleTitle[cntTitle]=myxmlPullParser.nextText();
					 cntTitle++;
				}else if (myxmlPullParser.getName().equals("SmartphoneUrl")){
					mArticleURL[cntAddress]=myxmlPullParser.nextText();
					cntAddress++;
				}

			}
		}
	}catch(XmlPullParserException e){
}catch(IOException e){
	}


}
}


