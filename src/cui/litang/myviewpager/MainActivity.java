package cui.litang.myviewpager;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int[] imgDrawables;   //图片id数据
	private String[] titles;   //图片id数据
	private ArrayList<ImageView> imageViews;   //ImageView 集合，用于轮播content
	private ArrayList<View> dots;
	private TextView tv_title;
	private ViewPager viewPager;
	protected int currentItem;
	private ScheduledExecutorService scheduledExecutorService;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgDrawables = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};
		
		titles = new String[imgDrawables.length];             //实例化标题
		titles[0] = "Win10来临，为何业界仍不看好微软";
		titles[1] = "法属留尼汪岛现飞机残骸 (组图)";
		titles[2] = "湖南游客穿越与斯巴达勇士作战";
		titles[3] = "查尔斯王子夫妇参加鲜花节";
		titles[4] = "台北市长柯文哲在超市吃泡面(图)";
		
		
		imageViews = new ArrayList<ImageView>();              //实例化图片元素
		for (int i = 0; i < imgDrawables.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imgDrawables[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}
		
		dots = new ArrayList<View>();			//实例化点元素
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(titles[0]);
		
		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new PagerAdapter() {           // 设置填充ViewPager页面的适配器
			
			/*
			 * Determines whether a page View is associated with a specific key object as returned by instantiateItem(ViewGroup, int).
			 *  This method is required for a PagerAdapter to function properly.
					Parameters:
					view Page View to check for association with object
					object Object to check for association with view
					Returns:
					true if view is associated with the key object object
			 */
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				
				return arg0==arg1;
			}
			
			@Override
			public int getCount() {
				
				return imgDrawables.length;
			}
			
			@Override
			public Object instantiateItem(View arg0, int arg1) {
				((ViewPager) arg0).addView(imageViews.get(arg1));
				return imageViews.get(arg1);
			}

			@Override
			public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPager) arg0).removeView((View) arg2);
			}


			
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			private int initPosition = 0;
			@Override
			public void onPageSelected(int position) {
				currentItem = position;
				tv_title.setText(titles[position]);
				dots.get(initPosition).setBackgroundResource(R.drawable.dot_normal);
				dots.get(position).setBackgroundResource(R.drawable.dot_focused);
				initPosition = position;
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
				//  Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				//  Auto-generated method stub
				
			}
		});
	}
	
	
	/**
	 * 以下部分全部为处理自动播放需要用到的代码。
	 */
		private Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
			};
		};
		
		
		
		@Override
		protected void onStart() {
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
			// 当Activity显示出来后，每两秒钟切换一次图片显示
			scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
			super.onStart();
		}

		@Override
		protected void onStop() {
			// 当Activity不可见的时候停止切换
			scheduledExecutorService.shutdown();
			super.onStop();
		}

		/**
		 * 换行切换任务
		 * 
		 * @author Administrator
		 * 
		 */
		private class ScrollTask implements Runnable {

			public void run() {
				synchronized (viewPager) {
					System.out.println("currentItem: " + currentItem);
					currentItem = (currentItem + 1) % imageViews.size();
					handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
				}
			}

		}
	

	
	
}
