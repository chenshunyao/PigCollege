/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.xnf.henghenghui.R;

import uk.co.senab.photoview.PhotoView;

public class ViewPagerActivity extends AppCompatActivity {

	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions mOptions;

	private String[] mDatas;
	private int curIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_pager);

        mDatas = getIntent().getStringArrayExtra("imglist");
        curIndex = getIntent().getIntExtra("curindex", 0);

		mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300))
				.build();

		ViewPager mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		setContentView(mViewPager);

		mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(curIndex,false);
	}

	private class SamplePagerAdapter extends PagerAdapter {

//		private static final int[] sDrawables = { R.drawable.em_welcom_logo, R.drawable.em_welcom_logo, R.drawable.em_welcom_logo,
//				R.drawable.em_welcom_logo, R.drawable.em_welcom_logo, R.drawable.em_welcom_logo };

		@Override
		public int getCount() {
			return mDatas.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
//			photoView.setImageResource(sDrawables[position]);
			mImageLoader.displayImage(mDatas[position],photoView);
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
