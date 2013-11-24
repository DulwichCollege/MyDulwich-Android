package uk.org.dulwich.mydulwich;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Notice {
	public final String title;
    public final String link;
    public final String content;
    private final String imageUrl;
    public final String author;
    public final String pubDate;
	public final String rankDate;
	public final String startDate;
	public final String expiryDate;
	public final String onHomepage;
	public final String attachments;
	public final Bitmap image;

    public Notice(String title, String description, String link, String author, String pubDate) {
        this.title = title;
        this.link = link;
        this.author = author;
        this.pubDate = pubDate;
        Document doc = Jsoup.parseBodyFragment(description);
        Element body = doc.body();
        Elements facts = body.children();
        Iterator<Element> iterator = facts.iterator();
        List<String> list = new ArrayList<String>();
        while (iterator.hasNext())
        {
        	Element e = iterator.next().child(0);
        	list.add(e.text());
        	e.remove();
        }
        ListIterator<String> i = (ListIterator<String>) list.listIterator();
        // This could be done much better.... I just made the best of a bad situation ok...
        if (i.next().equals("Body:")) this.content = facts.get(i.previousIndex()).html().trim();
        else { this.content = ""; i.previous(); }
        if (i.next().equals("AnnouncementImageUrl:")) this.imageUrl = facts.get(i.previousIndex()).child(0).attr("href");
        else { this.imageUrl = ""; i.previous(); }
        if (i.next().equals("RankDate:")) this.rankDate = facts.get(i.previousIndex()).html().trim();
        else { this.rankDate = ""; i.previous(); }
        if (i.next().equals("StartsOn:")) this.startDate = facts.get(i.previousIndex()).html().trim();
        else { this.startDate = ""; i.previous(); }
        if (i.next().equals("Expires:")) this.expiryDate = facts.get(i.previousIndex()).html().trim();
        else { this.expiryDate = ""; i.previous(); }
        if (i.next().equals("OnHomePage:")) this.onHomepage = facts.get(i.previousIndex()).html().trim();
        else { this.onHomepage = ""; i.previous(); }
        //if (i.next().equals("DisplayonOtherSites:")) this.sites = facts.get(i.previousIndex()).html().trim();
        //else { this.sites = ""; i.previous(); } // Waste of time. Might implement later.
        if (i.next().equals("Attachments:")) this.attachments = facts.get(i.previousIndex()).html().trim();
        else { this.attachments = ""; i.previous(); }
        
        Bitmap image = null;
		try {
        if (imageUrl.length() > 0)
			image = BitmapFactory.decodeStream((new URL(imageUrl)).openConnection().getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			try
			{
				image = BitmapFactory.decodeStream((new Request(StudentHome.instance, imageUrl).fetch().getContent()));
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		this.image = image;
    }
}