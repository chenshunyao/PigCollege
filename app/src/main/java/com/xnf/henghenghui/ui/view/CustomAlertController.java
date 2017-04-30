package com.xnf.henghenghui.ui.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.xnf.henghenghui.R;

import java.lang.ref.WeakReference;


public class CustomAlertController {

	private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

	protected final Context mContext;
	private final DialogInterface mDialogInterface;
	private final Window mWindow;

	private CharSequence mTitle;

	private CharSequence mMessage;

	private ListView mListView;

	private View mView;

	private int mViewSpacingLeft;

	private int mViewSpacingTop;

	private int mViewSpacingRight;

	private int mViewSpacingBottom;

	private boolean mViewSpacingSpecified = false;

	private Button mButtonPositive;

	private CharSequence mButtonPositiveText;

	private Message mButtonPositiveMessage;

	private Button mButtonNegative;

	private CharSequence mButtonNegativeText;

	private Message mButtonNegativeMessage;

	private Button mButtonNeutral;

	private CharSequence mButtonNeutralText;

	private Message mButtonNeutralMessage;

	private ScrollView mScrollView;

	private int mIconId = 0; // Ϊ0Ĭ������ͼ�꣬Ϊ-1����ʾ

	private Drawable mIcon;

	private ImageView mIconView;

	private TextView mTitleView;

	private TextView mMessageView;

	private View mCustomTitleView;

	protected boolean mForceInverseBackground;

	private ListAdapter mAdapter;

	private int mCheckedItem = -1;

	private Handler mHandler;

	public static int m_MyAlertContentViewId = R.layout.alert_dialog;

	View.OnClickListener mButtonHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Message m = null;
			if (v == mButtonPositive && mButtonPositiveMessage != null) {
				m = Message.obtain(mButtonPositiveMessage);
			} else if (v == mButtonNegative && mButtonNegativeMessage != null) {
				m = Message.obtain(mButtonNegativeMessage);
			} else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
				m = Message.obtain(mButtonNeutralMessage);
			}
			if (m != null) {
				m.sendToTarget();
			}

			// Post a message so we dismiss after the above handlers are
			// executed
			mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_dialog,
					mDialogInterface).sendToTarget();
		}
	};

	private static final class ButtonHandler extends Handler {
		// Button clicks have Message.what as the BUTTON{1,2,3} constant
		private static final int MSG_DISMISS_dialog = 1;

		private WeakReference<DialogInterface> mdialog;

		public ButtonHandler(DialogInterface dialog) {
			mdialog = new WeakReference<DialogInterface>(dialog);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case DialogInterface.BUTTON_POSITIVE:
			case DialogInterface.BUTTON_NEGATIVE:
			case DialogInterface.BUTTON_NEUTRAL:
				((DialogInterface.OnClickListener) msg.obj).onClick(
						mdialog.get(), msg.what);
				break;

			case MSG_DISMISS_dialog:
				((DialogInterface) msg.obj).dismiss();
			}
		}
	}

	public CustomAlertController(Context context, DialogInterface di, Window window) {
		mContext = context;
		mDialogInterface = di;
		mWindow = window;
		mHandler = new ButtonHandler(di);
	}

	static boolean canTextInput(View v) {
		if (v.onCheckIsTextEditor()) {
			return true;
		}

		if (!(v instanceof ViewGroup)) {
			return false;
		}

		ViewGroup vg = (ViewGroup) v;
		int i = vg.getChildCount();
		while (i > 0) {
			i--;
			v = vg.getChildAt(i);
			if (canTextInput(v)) {
				return true;
			}
		}

		return false;
	}

	public void installContent() {
		/* We use a custom title so never request a window title */
		mWindow.requestFeature(Window.FEATURE_NO_TITLE);

		if (mView == null || !canTextInput(mView)) {
			mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
					WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		}
		mWindow.setContentView(m_MyAlertContentViewId);
		setupView();
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	/**
	 */
	public void setCustomTitle(View customTitleView) {
		mCustomTitleView = customTitleView;
	}

	public void setMessage(CharSequence message) {
		mMessage = message;
		if (mMessageView != null) {
			mMessageView.setText(message);
		}
	}

	/**
	 * Set the view to display in the dialog.
	 */
	public void setView(View view) {
		mView = view;
		mViewSpacingSpecified = false;
	}

	/**
	 * Set the view to display in the dialog along with the spacing around that
	 * view
	 */
	public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
			int viewSpacingRight, int viewSpacingBottom) {
		mView = view;
		mViewSpacingSpecified = true;
		mViewSpacingLeft = viewSpacingLeft;
		mViewSpacingTop = viewSpacingTop;
		mViewSpacingRight = viewSpacingRight;
		mViewSpacingBottom = viewSpacingBottom;
	}

	/**
	 * Sets a click listener or a message to be sent when the button is clicked.
	 * You only need to pass one of {@code listener} or {@code msg}.
	 * 
	 * @param whichButton
	 *            Which button, can be one of
	 *            {@link DialogInterface#BUTTON_POSITIVE},
	 *            {@link DialogInterface#BUTTON_NEGATIVE}, or
	 *            {@link DialogInterface#BUTTON_NEUTRAL}
	 * @param text
	 *            The text to display in positive button.
	 * @param listener
	 *            The {@link DialogInterface.OnClickListener} to use.
	 * @param msg
	 *            The {@link Message} to be sent when clicked.
	 */
	public void setButton(int whichButton, CharSequence text,
			DialogInterface.OnClickListener listener, Message msg) {

		if (msg == null && listener != null) {
			msg = mHandler.obtainMessage(whichButton, listener);
		}

		switch (whichButton) {

		case DialogInterface.BUTTON_POSITIVE:
			mButtonPositiveText = text;
			mButtonPositiveMessage = msg;
			break;

		case DialogInterface.BUTTON_NEGATIVE:
			mButtonNegativeText = text;
			mButtonNegativeMessage = msg;
			break;

		case DialogInterface.BUTTON_NEUTRAL:
			mButtonNeutralText = text;
			mButtonNeutralMessage = msg;
			break;

		default:
			throw new IllegalArgumentException("Button does not exist");
		}
	}

	/**
	 * Set resId to 0 if you don't want an icon.
	 * 
	 * @param resId
	 *            the resourceId of the drawable to use as the icon or 0 if you
	 *            don't want an icon.
	 */
	public void setIcon(int resId) {
		mIconId = resId;
		if (mIconView != null) {
			if (resId > 0) {
				mIconView.setImageResource(mIconId);
			} else if (resId == 0) {
				mIconView.setVisibility(View.GONE);
			}
		}
	}

	public void setIcon(Drawable icon) {
		mIcon = icon;
		if ((mIconView != null) && (mIcon != null)) {
			mIconView.setImageDrawable(icon);
		}
	}

	public void setInverseBackgroundForced(boolean forceInverseBackground) {
		mForceInverseBackground = forceInverseBackground;
	}

	public ListView getListView() {
		return mListView;
	}

	public Button getButton(int whichButton) {
		switch (whichButton) {
		case DialogInterface.BUTTON_POSITIVE:
			return mButtonPositive;
		case DialogInterface.BUTTON_NEGATIVE:
			return mButtonNegative;
		case DialogInterface.BUTTON_NEUTRAL:
			return mButtonNeutral;
		default:
			return null;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return mScrollView != null && mScrollView.executeKeyEvent(event);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return mScrollView != null && mScrollView.executeKeyEvent(event);
	}

	private void setupView() {
		LinearLayout contentPanel = (LinearLayout) mWindow
				.findViewById(R.id.contentPanel);
		setupContent(contentPanel);
		boolean hasButtons = setupButtons();

		LinearLayout topPanel = (LinearLayout) mWindow
				.findViewById(R.id.topPanel);
		// TypedArray a = mContext.obtainStyledAttributes(
		// null, null, android.R.attr.alertdialogStyle, 0);
		TypedArray a = null;
		boolean hasTitle = setupTitle(topPanel);

		View buttonPanel = mWindow.findViewById(R.id.buttonPanel);
		if (!hasButtons) {
			buttonPanel.setVisibility(View.GONE);
		}

		FrameLayout customPanel = null;
		if (mView != null) {
			customPanel = (FrameLayout) mWindow.findViewById(R.id.customPanel);
			FrameLayout custom = (FrameLayout) mWindow
					.findViewById(R.id.custom);
			custom.addView(mView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
			if (mViewSpacingSpecified) {
				custom.setPadding(mViewSpacingLeft, mViewSpacingTop,
						mViewSpacingRight, mViewSpacingBottom);
			}
			if (mListView != null) {
				((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0;
			}
		} else {
			mWindow.findViewById(R.id.customPanel).setVisibility(View.GONE);
		}

		/*
		 * Only display the divider if we have a title and a custom view or a
		 * message.
		 */
		if (hasTitle && ((mMessage != null) || (mView != null))) {
			// View divider = mWindow.findViewById(R.id.titleDivider);
			// divider.setVisibility(View.VISIBLE);
		}

		setBackground(topPanel, contentPanel, customPanel, hasButtons, a,
				hasTitle, buttonPanel);
		// a.recycle();
	}

	private boolean setupTitle(LinearLayout topPanel) {
		boolean hasTitle = true;

		if (mCustomTitleView != null) {
			// Add the custom title view directly to the topPanel layout
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);

			topPanel.addView(mCustomTitleView, lp);

			// Hide the title template
			View titleTemplate = mWindow.findViewById(R.id.title_template);
			titleTemplate.setVisibility(View.GONE);
		} else {
			final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);

			mIconView = (ImageView) mWindow.findViewById(R.id.icon);
			if (hasTextTitle) {

				/* Display the title if a title is supplied, else hide it */
				mTitleView = (TextView) mWindow.findViewById(R.id.alertTitle);

				mTitleView.setText(mTitle);
				// mIconView.setImageResource(R.drawable.ic_dialog_menu_generic);

				/*
				 * Do this last so that if the user has supplied any icons we
				 * use them instead of the default ones. If the user has
				 * specified 0 then make it disappear.
				 */
				if (mIconId > 0) {
					mIconView.setImageResource(mIconId);
				} else if (mIcon != null) {
					mIconView.setImageDrawable(mIcon);
				} else if (mIconId == 0) {

					/*
					 * Apply the padding from the icon to ensure the title is
					 * aligned correctly.
					 */
					mTitleView.setPadding(mIconView.getPaddingLeft(),
							mIconView.getPaddingTop(),
							mIconView.getPaddingRight(),
							mIconView.getPaddingBottom());
					mIconView.setVisibility(View.GONE);
				}
			} else {

				// Hide the title template
				View titleTemplate = mWindow.findViewById(R.id.title_template);
				titleTemplate.setVisibility(View.GONE);
				mIconView.setVisibility(View.GONE);
				hasTitle = false;
			}
		}
		return hasTitle;
	}

	private void setupContent(LinearLayout contentPanel) {
		mScrollView = (ScrollView) mWindow.findViewById(R.id.scrollView);
		mScrollView.setFocusable(false);

		// Special case for users that only want to display a String
		mMessageView = (TextView) mWindow.findViewById(R.id.message);
		if (mMessageView == null) {
			return;
		}

		if (mMessage != null) {
			mMessageView.setText(mMessage);
		} else {
			mMessageView.setVisibility(View.GONE);
			mScrollView.removeView(mMessageView);

			if (mListView != null) {
				contentPanel.removeView(mWindow.findViewById(R.id.scrollView));
				contentPanel.addView(mListView, new LinearLayout.LayoutParams(
						MATCH_PARENT, MATCH_PARENT));
				contentPanel.setLayoutParams(new LinearLayout.LayoutParams(
						MATCH_PARENT, 0, 1.0f));
			} else {
				contentPanel.setVisibility(View.GONE);
			}
		}
	}

	private boolean setupButtons() {
		int BIT_BUTTON_POSITIVE = 1;
		int BIT_BUTTON_NEGATIVE = 2;
		int BIT_BUTTON_NEUTRAL = 4;
		int whichButtons = 0;
		mButtonPositive = (Button) mWindow.findViewById(R.id.button1);
		mButtonPositive.setOnClickListener(mButtonHandler);

		if (TextUtils.isEmpty(mButtonPositiveText)) {
			mButtonPositive.setVisibility(View.GONE);
		} else {
			mButtonPositive.setText(mButtonPositiveText);
			mButtonPositive.setVisibility(View.VISIBLE);
			whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
		}

		mButtonNegative = (Button) mWindow.findViewById(R.id.button2);
		mButtonNegative.setOnClickListener(mButtonHandler);

		if (TextUtils.isEmpty(mButtonNegativeText)) {
			mButtonNegative.setVisibility(View.GONE);
		} else {
			mButtonNegative.setText(mButtonNegativeText);
			mButtonNegative.setVisibility(View.VISIBLE);

			whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
		}

		mButtonNeutral = (Button) mWindow.findViewById(R.id.button3);
		mButtonNeutral.setOnClickListener(mButtonHandler);

		if (TextUtils.isEmpty(mButtonNeutralText)) {
			mButtonNeutral.setVisibility(View.GONE);
		} else {
			mButtonNeutral.setText(mButtonNeutralText);
			mButtonNeutral.setVisibility(View.VISIBLE);

			whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
		}

		/*
		 * If we only have 1 button it should be centered on the layout and
		 * expand to fill 50% of the available space.
		 */
		if (whichButtons == BIT_BUTTON_POSITIVE) {
			centerButton(mButtonPositive);
		} else if (whichButtons == BIT_BUTTON_NEGATIVE) {
			centerButton(mButtonNeutral);
		} else if (whichButtons == BIT_BUTTON_NEUTRAL) {
			centerButton(mButtonNeutral);
		}

		return whichButtons != 0;
	}

	private void centerButton(Button button) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button
				.getLayoutParams();
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.weight = 0.5f;
		button.setLayoutParams(params);
		View leftSpacer = mWindow.findViewById(R.id.leftSpacer);
		leftSpacer.setVisibility(View.VISIBLE);
		View rightSpacer = mWindow.findViewById(R.id.rightSpacer);
		rightSpacer.setVisibility(View.VISIBLE);
	}

	private void setBackground(LinearLayout topPanel,
			LinearLayout contentPanel, View customPanel, boolean hasButtons,
			TypedArray axxd, boolean hasTitle, View buttonPanel) {

		/* Get all the different background required */
		int fullDark = R.drawable.popup_full;
		int topDark = R.drawable.popup_top;
		int centerDark = R.drawable.popup_center;
		int bottomDark = R.drawable.popup_bottom;
		int fullBright = R.drawable.popup_full;
		int topBright = R.drawable.popup_top;
		int centerBright = R.drawable.popup_center;
		int bottomBright = R.drawable.popup_bottom;
		int bottomMedium = R.drawable.popup_bottom;

		/*
		 * We now set the background of all of the sections of the alert. First
		 * collect together each section that is being displayed along with
		 * whether it is on a light or dark background, then run through them
		 * setting their backgrounds. This is complicated because we need to
		 * correctly use the full, top, middle, and bottom graphics depending on
		 * how many views they are and where they appear.
		 */

		View[] views = new View[4];
		boolean[] light = new boolean[4];
		View lastView = null;
		boolean lastLight = false;

		int pos = 0;
		if (hasTitle) {
			views[pos] = topPanel;
			light[pos] = true;
			pos++;
		}

		/*
		 * The contentPanel displays either a custom text message or a ListView.
		 * If it's text we should use the dark background for ListView we should
		 * use the light background. If neither are there the contentPanel will
		 * be hidden so set it as null.
		 */
		views[pos] = (contentPanel.getVisibility() == View.GONE) ? null
				: contentPanel;
		light[pos] = true;// mListView != null;
		pos++;
		if (customPanel != null) {
			views[pos] = customPanel;
			light[pos] = true;// mForceInverseBackground;
			pos++;
		}
		if (hasButtons) {
			views[pos] = buttonPanel;
			light[pos] = true;
		}

		boolean setView = false;
		for (pos = 0; pos < views.length; pos++) {
			View v = views[pos];
			if (v == null) {
				continue;
			}
			if (lastView != null) {
				if (!setView) {
					lastView.setBackgroundResource(lastLight ? topBright
							: topDark);
				} else {
					lastView.setBackgroundResource(lastLight ? centerBright
							: centerDark);
				}
				setView = true;
			}
			lastView = v;
			lastLight = light[pos];
		}

		if (lastView != null) {
			if (setView) {

				/*
				 * ListViews will use the Bright background but buttons use the
				 * Medium background.
				 */
				lastView.setBackgroundResource(lastLight ? (hasButtons ? bottomMedium
						: bottomBright)
						: bottomDark);
			} else {
				lastView.setBackgroundResource(lastLight ? fullBright
						: fullDark);
			}
		}

		/*
		 * TODO: uncomment section below. The logic for this should be if it's a
		 * Contextual menu being displayed AND only a Cancel button is shown
		 * then do this.
		 */
		// if (hasButtons && (mListView != null)) {

		/*
		 * Yet another *special* case. If there is a ListView with buttons don't
		 * put the buttons on the bottom but instead put them in the footer of
		 * the ListView this will allow more items to be displayed.
		 */

		/*
		 * contentPanel.setBackgroundResource(bottomBright);
		 * buttonPanel.setBackgroundResource(centerMedium); ViewGroup parent =
		 * (ViewGroup) mWindow.findViewById(R.id.parentPanel);
		 * parent.removeView(buttonPanel); AbsListView.LayoutParams params = new
		 * AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT,
		 * AbsListView.LayoutParams.MATCH_PARENT);
		 * buttonPanel.setLayoutParams(params);
		 * mListView.addFooterView(buttonPanel);
		 */
		// }

		if ((mListView != null) && (mAdapter != null)) {
			mListView.setAdapter(mAdapter);
			// Begin: modify by zhijun.dong 001131 2014-4-16 for remove divider when listview has only one item 
			if(mAdapter.getCount() <= 1){
				mListView.setDivider(null);
			}
			// End: modify by zhijun.dong 001131 2014-4-16 for remove divider when listview has only one item 
			if (mCheckedItem > -1) {
				mListView.setItemChecked(mCheckedItem, true);
				mListView.setSelection(mCheckedItem);
			}
		}
	}

	public static class RecycleListView extends ListView {
		boolean mRecycleOnMeasure = true;

		public RecycleListView(Context context) {
			super(context);
		}

		public RecycleListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public RecycleListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		protected boolean recycleOnMeasure() {
			return mRecycleOnMeasure;
		}
	}

	public static class AlertParams {
		public final Context mContext;
		public final LayoutInflater mInflater;

		public int mIconId = -1;
		public Drawable mIcon;
		public CharSequence mTitle;
		public View mCustomTitleView;
		public CharSequence mMessage;
		public CharSequence mPositiveButtonText;
		public DialogInterface.OnClickListener mPositiveButtonListener;
		public CharSequence mNegativeButtonText;
		public DialogInterface.OnClickListener mNegativeButtonListener;
		public CharSequence mNeutralButtonText;
		public DialogInterface.OnClickListener mNeutralButtonListener;
		public boolean mCancelable;
		public DialogInterface.OnCancelListener mOnCancelListener;
		public DialogInterface.OnKeyListener mOnKeyListener;
		public CharSequence[] mItems;
		public ListAdapter mAdapter;
		public DialogInterface.OnClickListener mOnClickListener;
		public View mView;
		public int mViewSpacingLeft;
		public int mViewSpacingTop;
		public int mViewSpacingRight;
		public int mViewSpacingBottom;
		public boolean mViewSpacingSpecified = false;
		public boolean[] mCheckedItems;
		public boolean mIsMultiChoice;
		public boolean mIsSingleChoice;
		public int mCheckedItem = -1;
		public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
		public Cursor mCursor;
		public String mLabelColumn;
		public String mIsCheckedColumn;
		public boolean mForceInverseBackground;
		public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
		public OnPrepareListViewListener mOnPrepareListViewListener;
		public boolean mRecycleOnMeasure = true;

		/**
		 * Interface definition for a callback to be invoked before the ListView
		 * will be bound to an adapter.
		 */
		public interface OnPrepareListViewListener {

			/**
			 * Called before the ListView is bound to an adapter.
			 * 
			 * @param listView
			 *            The ListView that will be shown in the dialog.
			 */
			void onPrepareListView(ListView listView);
		}

		public AlertParams(Context context) {
			mContext = context;
			mCancelable = true;
			mInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public void apply(CustomAlertController dialog) {
			if (mCustomTitleView != null) {
				dialog.setCustomTitle(mCustomTitleView);
			} else {
				if (mTitle != null) {
					dialog.setTitle(mTitle);
				}
				if (mIcon != null) {
					dialog.setIcon(mIcon);
				}
				if (mIconId >= 0) {
					dialog.setIcon(mIconId);
				}
			}
			if (mMessage != null) {
				dialog.setMessage(mMessage);
			}
			if (mPositiveButtonText != null) {
				dialog.setButton(DialogInterface.BUTTON_POSITIVE,
						mPositiveButtonText, mPositiveButtonListener, null);
			}
			if (mNegativeButtonText != null) {
				dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
						mNegativeButtonText, mNegativeButtonListener, null);
			}
			if (mNeutralButtonText != null) {
				dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
						mNeutralButtonText, mNeutralButtonListener, null);
			}
			if (mForceInverseBackground) {
				dialog.setInverseBackgroundForced(true);
			}
			// For a list, the client can either supply an array of items or an
			// adapter or a cursor
			if ((mItems != null) || (mCursor != null) || (mAdapter != null)) {
				createListView(dialog);
			}
			if (mView != null) {
				if (mViewSpacingSpecified) {
					dialog.setView(mView, mViewSpacingLeft, mViewSpacingTop,
							mViewSpacingRight, mViewSpacingBottom);
				} else {
					dialog.setView(mView);
				}
			}

			/*
			 * dialog.setCancelable(mCancelable);
			 * dialog.setOnCancelListener(mOnCancelListener); if (mOnKeyListener
			 * != null) { dialog.setOnKeyListener(mOnKeyListener); }
			 */
		}

		private void createListView(final CustomAlertController dialog) {
			final RecycleListView listView = (RecycleListView) mInflater
					.inflate(R.layout.select_dialog, null);
			ListAdapter adapter;

			if (mIsMultiChoice) {
				if (mCursor == null) {
					adapter = new ArrayAdapter<CharSequence>(mContext,
							R.layout.select_dialog_multichoice,
							android.R.id.text1, mItems) {
						@Override
						public View getView(int position, View convertView,
								ViewGroup parent) {
							View view = super.getView(position, convertView,
									parent);
							if (mCheckedItems != null) {
								boolean isItemChecked = mCheckedItems[position];
								if (isItemChecked) {
									listView.setItemChecked(position, true);
								}
							}
							return view;
						}
					};
				} else {
					adapter = new CursorAdapter(mContext, mCursor, false) {
						private final int mLabelIndex;
						private final int mIsCheckedIndex;

						{
							final Cursor cursor = getCursor();
							mLabelIndex = cursor
									.getColumnIndexOrThrow(mLabelColumn);
							mIsCheckedIndex = cursor
									.getColumnIndexOrThrow(mIsCheckedColumn);
						}

						@Override
						public void bindView(View view, Context context,
								Cursor cursor) {
							CheckedTextView text = (CheckedTextView) view
									.findViewById(android.R.id.text1);
							text.setText(cursor.getString(mLabelIndex));
							listView.setItemChecked(cursor.getPosition(),
									cursor.getInt(mIsCheckedIndex) == 1);
						}

						@Override
						public View newView(Context context, Cursor cursor,
								ViewGroup parent) {
							return mInflater.inflate(
									R.layout.select_dialog_multichoice,
									parent, false);
						}

					};
				}
			} else {
				int layout = mIsSingleChoice ? R.layout.select_dialog_singlechoice
						: R.layout.select_dialog_item;
				if (mCursor == null) {
					adapter = (mAdapter != null) ? mAdapter
							: new ArrayAdapter<CharSequence>(mContext, layout,
									android.R.id.text1, mItems);
				} else {
					adapter = new SimpleCursorAdapter(mContext, layout,
							mCursor, new String[] { mLabelColumn },
							new int[] { android.R.id.text1 });
				}
			}

			if (mOnPrepareListViewListener != null) {
				mOnPrepareListViewListener.onPrepareListView(listView);
			}

			/*
			 * Don't directly set the adapter on the ListView as we might want
			 * to add a footer to the ListView later.
			 */
			dialog.mAdapter = adapter;
			dialog.mCheckedItem = mCheckedItem;

			if (mOnClickListener != null) {
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					@SuppressWarnings("rawtypes")
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {
						mOnClickListener.onClick(dialog.mDialogInterface,
								position);
						if (!mIsSingleChoice) {
							dialog.mDialogInterface.dismiss();
						}
					}
				});
			} else if (mOnCheckboxClickListener != null) {
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					@SuppressWarnings("rawtypes")
					public void onItemClick(AdapterView parent, View v,
							int position, long id) {
						if (mCheckedItems != null) {
							mCheckedItems[position] = listView
									.isItemChecked(position);
						}
						mOnCheckboxClickListener.onClick(
								dialog.mDialogInterface, position,
								listView.isItemChecked(position));
					}
				});
			}

			// Attach a given OnItemSelectedListener to the ListView
			if (mOnItemSelectedListener != null) {
				listView.setOnItemSelectedListener(mOnItemSelectedListener);
			}

			if (mIsSingleChoice) {
				listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
			} else if (mIsMultiChoice) {
				listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
			}
			listView.mRecycleOnMeasure = mRecycleOnMeasure;
			dialog.mListView = listView;
		}
	}

}
