package uk.org.dulwich.mydulwich;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeAdapter extends ArrayAdapter<Notice> {
	private ArrayList<Notice> notices;
	private Context context;

	public NoticeAdapter(Context context, int resource, ArrayList<Notice> objects)
	{
		super(context, resource, objects);
		this.notices = objects;
		this.context = context;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		if (view == null)
		{
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.notice, null);
        }
		Notice notice = notices.get(position);
		if (notice != null)
		{
			ImageView im = (ImageView) view.findViewById(R.id.noticeImage);
			TextView tt = (TextView) view.findViewById(R.id.noticeTitle);
            TextView bt = (TextView) view.findViewById(R.id.noticeContent);
            if (im != null) if(notice.image != null) im.setImageBitmap(notice.image);
            if (tt != null) tt.setText(notice.title);
            if (bt != null) bt.setText(Html.fromHtml(notice.content));
		}
		return view;
	}
}
