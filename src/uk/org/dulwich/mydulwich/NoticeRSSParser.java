package uk.org.dulwich.mydulwich;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class NoticeRSSParser {
	public List<Notice> parse(InputStream in) throws XmlPullParserException, IOException
	{
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
	}
	
	private List<Notice> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
	{
	    List<Notice> entries = new ArrayList<Notice>();
	    parser.require(XmlPullParser.START_TAG, null, "rss");
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) continue;
	        String name = parser.getName();
	        // Starts by looking for the channel tag
	        if (name.equals("channel")) {
	        	while (parser.next() != XmlPullParser.END_TAG) {
	    	        if (parser.getEventType() != XmlPullParser.START_TAG) continue;
	    	        name = parser.getName();
	    	        // Then look for an item tag
	    	        if (name.equals("item")) entries.add(readNotice(parser));
	    	        else skip(parser);
	    	    }
	        } else skip(parser);
	    }
	    return entries;
	}
	
	private Notice readNotice(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, null, "item");
	    String title = null;
	    String summary = null;
	    String link = null;
	    String author = null;
	    String date = null;
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        String name = parser.getName();
	        if (name.equals("title")) {
	            title = readTag(parser, "title");
	        } else if (name.equals("description")) {
	            summary = readBody(parser);
	        } else if (name.equals("link")) {
	            link = readTag(parser, "link");
	        } else if (name.equals("author")) {
	            author = readTag(parser, "author");
	        } else if (name.equals("pubDate")) {
	            date = readTag(parser, "pubDate");
	        } else {
	            skip(parser);
	        }
	    }
	    return new Notice(title, summary, link, author, date);
	}
	
	private String readBody(XmlPullParser parser) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, null, "description");
	    String string = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, "description");
	    return string;
	}

	private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
	    parser.require(XmlPullParser.START_TAG, null, tag);
	    String string = readText(parser);
	    parser.require(XmlPullParser.END_TAG, null, tag);
	    return string;
	}

	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}