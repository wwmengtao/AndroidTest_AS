package com.fernandocejas.android10.sample.data.database.xmlOps;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import com.fernandocejas.android10.sample.data.ALog;
import com.fernandocejas.android10.sample.data.entity.UserEntity;
import com.fernandocejas.android10.sample.domain.interactor.GetUserListDetails;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

public class XmlOperator {
	private static final String NoviceAssetsXmlDir = "novicetutorial"+ File.separator+"xmlfiles";//玩家教程存储xml文件的Assets根目录
	private XmlPullParser mXmlPullParser = null;//读取xml文件
	private String ioEncoding=null;
	private InputStream mInputStream=null;
	private Context mContext=null;
	private AssetManager mAssetManager = null;
	private Collection<UserEntity> mUserEntityCollection = null;
	//
	public XmlOperator(Context context){
		mContext=context.getApplicationContext();
		ioEncoding = "UTF-8";//"ISO-8859-1","UTF-16BE","UTF-16LE"
		mXmlPullParser = Xml.newPullParser();
		mAssetManager = mContext.getAssets();
		mUserEntityCollection = new ArrayList<>();
	}

	public void howToReadXml(Context context){
		String fileName="writeXml.xml";
		String tagOfDoc="tagOfDoc";
		String eleName="eleName";
		String attr="attr";
		XmlOperator mXmlOperator=new XmlOperator(context);
		mXmlOperator.readFromXml("");
	}


	public void readFromXml(String fileName){
		try {
			mInputStream = mAssetManager.open(fileName);
			mXmlPullParser.setInput(new BufferedInputStream(mInputStream), ioEncoding);
			filterBeforeFirstElement(mXmlPullParser, "file");
			parseXml(mXmlPullParser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(null!=mInputStream){
				try {
					mInputStream.close();
					mInputStream=null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void parseXml(XmlPullParser parser){
		int type;
		try {
			while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
				switch (type) {
					case XmlPullParser.START_TAG:
						if (parser.getName().equals("item")) {
							parseItemType1(parser);
						}
						break;
					case XmlPullParser.END_TAG:
						if (parser.getName().equals("mblog")) {
						}
						break;
					default:
						break;
				}
			}//end while
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e){
			e.printStackTrace();
		}
	}
	private String[] LEVEL1_ITEM_TAGS={"filename","filestring","filebackpic"};
	private boolean isLEVEL1_ITEM_TAGS(String tag){
		if(null == tag)return false;
		for(String level1:LEVEL1_ITEM_TAGS){
			if(level1.equals(tag)){
				return true;
			}
		}
		return false;
	}

	private void parseItemType1(XmlPullParser parser) throws XmlPullParserException,IOException,ParseException {
		int count = 0;
		int type;
		StringBuffer str = new StringBuffer("");
		while (true) {
			type = parser.next();
			switch (type){
				case XmlPullParser.START_TAG:
					str.append(parser.getName()+": ");
					break;
				case XmlPullParser.TEXT:
					str.append(parser.getText().trim());
					break;
				case XmlPullParser.END_TAG:
					count ++;
					ALog.Log("parseItemType1: "+str.toString());
					str.setLength(0);
					if(LEVEL1_ITEM_TAGS.length == count)return;
					break;
			}
		}
	}
	/**
	 * filterBeforeFirstElement：过滤掉达到指定标签firstElementName之前的所有内容
	 * @param parser
	 * @param firstElementName
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public void filterBeforeFirstElement(XmlPullParser parser, String firstElementName)
			throws XmlPullParserException, IOException{
		int type;
		//首先过滤掉非标签类事件
		while ((type=parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
			;
		}
		//已经到了END_DOCUMENT
		if (type != XmlPullParser.START_TAG) {
			throw new XmlPullParserException("No start tag");
		}
		//已经到了START_TAG
		if (!parser.getName().equals(firstElementName)) {
			throw new XmlPullParserException("Unexpected start tag: "+parser.getName()+", expected: " + firstElementName);
		}
	}

	/**
	 * 读取xml文件中的数据写入数据列表并且返回
	 * @param params
	 * @return
	 */
	public Collection<UserEntity> UserEntityCollectionXml(GetUserListDetails.Params params){
		mUserEntityCollection.clear();
		if(params.getDataType() == GetUserListDetails.Params.DataType.COLLECTION_DATA_LEVEL1){
			readFromXml(NoviceAssetsXmlDir+File.separator+params.getFileName());
		}
		return mUserEntityCollection;
	}

	private void getXmlData(){
		int resID = mContext.getResources().getIdentifier(
				"app_name","string",mContext.getPackageName());
		ALog.Log("createDBDataStore: "+mContext.getResources().getString(resID));

		String []files = null;
		try {
			files = mAssetManager.list(NoviceAssetsXmlDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(null!=files&&files.length>0){
			for(String file:files){
				ALog.Log("createDBDataStore2: "+file);
			}
		}
	}
}
