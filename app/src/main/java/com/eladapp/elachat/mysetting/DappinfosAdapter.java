package com.eladapp.elachat.mysetting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import org.ela.Carrier.Chatcarrier;
import com.eladapp.elachat.R;
import com.eladapp.elachat.db.Db;
import com.eladapp.elachat.utils.ImageLoaders;

import java.util.Map;

public class DappinfosAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> datas;
    // public ImageLoader imageLoader;
    public DappinfosAdapter(Context context ,  List <Map<String, String>> datas){
        this.context = context;
        this.datas = datas;
        //imageLoader=new ImageLoader(context);
    }
    public Map getItem(int position) {
        return datas.get(position);
    }
    public long getItemId(int position) {
        return position;
    }
    /**
     * get count of messages
     */
    public int getCount() {
        return datas.size();
    }
    @SuppressLint("NewApi")
    public View getView(int i, View view, ViewGroup viewGroup) {
        final  ViewHolder vh;
        if (view == null){
            vh = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.activity_setting_dapplist_item, null);
            vh.dappimg = (ImageView)view.findViewById(R.id.dappinfo_img);
            vh.dappname = (TextView)view.findViewById(R.id.dappinfo_appname);
            vh.dappstatus = (TextView)view.findViewById(R.id.dappinfo_status);
            vh.dappid = (TextView) view.findViewById(R.id.dappinfo_appid);
            vh.dappinfoview = (Button) view.findViewById(R.id.dappinfo_viewinfo);
            vh.dappinfodelbtn = (Button) view.findViewById(R.id.dappinfo_delbtn);
            vh.dappinfomenu = (Button) view.findViewById(R.id.dappinfo_menulist);
            vh.dappdidinfo = (Button) view.findViewById(R.id.dappinfo_didinfo);
            vh.dappinfoid = (TextView) view.findViewById(R.id.dappinfo_dappid);
            view.setTag(vh);
            vh.dappinfoid.setText((String)datas.get(i).get("appid"));
            vh.dappimg.setImageBitmap(Base64ToBitmap((String)datas.get(i).get("images")));
            vh.dappname.setText((String)datas.get(i).get("appname"));
            vh.dappid.setText((String)datas.get(i).get("appid"));
            if(datas.get(i).get("did").equals("")){
                vh.dappstatus.setText("状态：未设置DID信息");
            }
            if(datas.get(i).get("menujson").equals("")){
                vh.dappstatus.setText("状态：未设置DAPP菜单信息");
            }
        } else {
            vh = (ViewHolder)view.getTag();
        }
        vh.dappinfomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("获取DPPID："+vh.dappinfoid.getText().toString());
            }
        });
        return view;
    }
    //Base64转化为图片
    public Bitmap Base64ToBitmap(String str){
        byte[] bytes = Base64.decode(str,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
    public final class ViewHolder
    {
        public ImageView dappimg;
        public TextView dappname;
        public TextView dappstatus;
        public TextView dappid;
        public Button dappinfoview;
        public Button dappinfodelbtn;
        public Button dappinfomenu;
        public Button dappdidinfo;
        public TextView dappinfoid;
    }
}