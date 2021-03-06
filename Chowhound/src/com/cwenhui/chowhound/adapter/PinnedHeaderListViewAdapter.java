package com.cwenhui.chowhound.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import za.co.immedia.pinnedheaderlistview.SectionedBaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cwenhui.chowhound.bean.GoodsBean;
import com.cwenhui.chowhound.config.Configs;
import com.cwenhui.chowhound.utils.AddGoodsAnim;
import com.cwenhui.chowhound.utils.ImageFirstDisplayListener;
import com.example.chowhound.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PinnedHeaderListViewAdapter extends SectionedBaseAdapter {
	private final String TAG = "PinnedHeaderListViewAdapter";
	private Context context;
	private List<String> leftMenu;
	private Map<String, List<GoodsBean>> rightGoods;
	private int lastSection = 0;
	private ImageLoader imageLoader = ImageLoader.getInstance();		//图片加载工具
	private DisplayImageOptions options;								//显示图片的各种设置
	private ImageFirstDisplayListener displayListener = new ImageFirstDisplayListener();
	private List<GoodsBean> selected = new ArrayList<GoodsBean>();		//被选中的商品的位置
	
//	private ViewGroup anim_mask_layout;									// 动画层,用来放在界面上跑的小图片
	private ImageView buyImg;											// 这是在界面上跑的小图片
	private int totalMoney = 0;											//总额
	private int iBuyNum = 0;											//商品购买的总数量
	private TextView sum;												//显示总额的textview
	private TextView buyNum;											//显示购买数量的textview
	private AddGoodsAnim anim;

	public PinnedHeaderListViewAdapter(Context context, List<String> leftMenu, Map<String, List<GoodsBean>> rightGoods, 
			TextView sum, TextView buyNum) {
		this.context = context;
		this.leftMenu = leftMenu;
		this.rightGoods = rightGoods;
		this.sum = sum;
		this.buyNum = buyNum;
		anim = new AddGoodsAnim(buyNum, context);
		initDisplayImageOptions();
	}

	/**
	 * 初始化展示图片需要的选项
	 */
	private void initDisplayImageOptions() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ddt_place_holder_brand_default) // 设置图片在下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ddt_place_holder_brand_default) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ddt_place_holder_brand_default) // 设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
		.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
		.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
		.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
		.build();
	}

	/**
	 * 返回商品
	 */
	@Override
	public Object getItem(int section, int position) {
		String classifyName = leftMenu.get(section);							//根据section从leftMenu找到分类名，对应的rightGoods的key
		List<GoodsBean> goods = rightGoods.get(classifyName);						//根据分类名(rightGoods的key)找到对应的商品列表
		return goods.get(position);												//根据position从商品列表中返回商品
	}

	/**
	 * 返回商品在对应分类中的位置
	 */
	@Override
	public long getItemId(int section, int position) {			//返回商品在section中的位置
		return position;
	}

	/**
	 * 返回分类的数目(即共几种分类)
	 */
	@Override
	public int getSectionCount() {
		return leftMenu.size();
	}

	/**
	 * 返回每种分类中商品的数目
	 */
	@Override
	public int getCountForSection(int section) {
		String classifyName = leftMenu.get(section);							//根据section从leftMenu找到分类名，对应的rightGoods的key
		List<GoodsBean> goods = rightGoods.get(classifyName);					//根据分类名(rightGoods的key)找到对应的商品列表
		return goods.size();
	}

	/**
	 * 实例化列表中每个item的样式(包括分类名的item和分类下的商品的item)
	 */
	@Override
	public View getItemView(final int section, final int position, View convertView,
			ViewGroup parent) {
		final Viewholder holder;
		if (convertView == null) {
			holder = new Viewholder();
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (LinearLayout) inflator.inflate(R.layout.item_fragment_menu_goods, null);		//此处借用classify的item布局
            holder.goodsName = (TextView) convertView.findViewById(R.id.tv_item_frag_menu_goods_name);
            holder.price = (TextView) convertView.findViewById(R.id.tv_item_frag_menu_goods_price);
            holder.goodsImg = (ImageView) convertView.findViewById(R.id.iv_item_frag_menu_goods_img);
            holder.add = (Button) convertView.findViewById(R.id.btn_item_frag_menu_goods_add);
            holder.del = (Button) convertView.findViewById(R.id.btn_item_frag_menu_goods_del);
            holder.selectedNum = (TextView) convertView.findViewById(R.id.tv_item_frag_menu_goods_selected_num);
            convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
        }
		holder.goodsName.setText(((GoodsBean)getItem(section, position)).getGoodsName());
		holder.price.setText(((GoodsBean)getItem(section, position)).getPrice()+"");			//将价格转换成字符串,否则会被当成id
		holder.del.setVisibility(View.INVISIBLE);
		holder.selectedNum.setVisibility(View.INVISIBLE);
		showGoodsOperation(section, position, holder);
		
		setGoodsOperateListener(section, position, holder, holder.add);
		setGoodsOperateListener(section, position, holder, holder.del);
		
		imageLoader.displayImage(Configs.APIShopUploadImg+((GoodsBean)getItem(section, position)).getGoodsImg(), 
				(ImageView)holder.goodsImg,options, displayListener); 
		
		return convertView;
	}

	/**
	 * 为增加、减少按钮设置监听
	 * @param section	数据的分类的位置
	 * @param position	数据在分类中的位置
	 * @param holder	holder用于获取view,对view进行操作
	 * @param btn		被点击的按钮
	 */
	private void setGoodsOperateListener(final int section, final int position,
			final Viewholder holder, Button btn) {
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_item_frag_menu_goods_add:
					goodsOperate(section, position, '+', v, holder);
					break;
				case R.id.btn_item_frag_menu_goods_del: 
					goodsOperate(section, position, '-', null, holder);
					break;
				default:
					break;
				}
			}
		});
	}
	
	/**
	 * 增加、减少购物车商品操作
	 * @param section	数据的分类的位置
	 * @param position	数据在分类中的位置
	 * @param operator	操作类型    +:添加     -:减少 
	 * @param v			被点击的按钮
	 * @param holder	holder用于获取view,对view进行操作
	 */
	public void goodsOperate(int section, int position, char operator, View v, Viewholder holder){
		int selectedNum = ((GoodsBean)getItem(section, position)).getSelectedNum();
		switch (operator) {
		case '+':
			((GoodsBean)getItem(section, position)).setSelectedNum(++selectedNum);
			if(selectedNum == 1){
				selected.add((GoodsBean)getItem(section, position));
			}
			int[] start_location = new int[2];								// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
			v.getLocationInWindow(start_location);							// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
			buyImg = new ImageView(context);
			buyImg.setImageResource(R.drawable.feature_point_cur);			// 设置buyImg的图片
			anim.setAddAnim(buyImg, start_location);						// 开始执行动画
			++iBuyNum;  										//统计购买数量
			buyNum.setText(iBuyNum+"");
			totalMoney +=  ((GoodsBean)getItem(section, position)).getPrice();	//计算总额
			break;
			
		case '-':
			((GoodsBean)getItem(section, position)).setSelectedNum(--selectedNum);
			if(selectedNum == 0){
				selected.remove((GoodsBean)getItem(section, position));
			}
			buyNum.setText(--iBuyNum+"");
			totalMoney -=  ((GoodsBean)getItem(section, position)).getPrice();	//计算总额
			break;
		default:
			break;
		}
		showGoodsOperation(section, position, holder);
		sum.setText(totalMoney+"元"); 
	}

	/**
	 * 显示商品操作控件(删除button和购买数量textview)
	 * @param section
	 * @param position
	 * @param holder
	 */
	private void showGoodsOperation(final int section, final int position,
			final Viewholder holder) { 
		if(((GoodsBean)getItem(section, position)).getSelectedNum()>0){
			holder.del.setVisibility(View.VISIBLE);
			holder.selectedNum.setVisibility(View.VISIBLE);
			holder.selectedNum.setText(((GoodsBean)getItem(section, position)).getSelectedNum()+"");
		}else{
			holder.del.setVisibility(View.INVISIBLE);
			holder.selectedNum.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		HeaderViewHolder holder;
		if (convertView == null) {
			holder = new HeaderViewHolder();
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (LinearLayout) inflator.inflate(R.layout.item_fragment_menu_goods_header, null);		//此处借用classify的item布局
            holder.header = (TextView) convertView.findViewById(R.id.tv_fragment_menu_goods_header);
            convertView.setTag(holder);
		} else {
			holder = (HeaderViewHolder) convertView.getTag();
        }
		if(lastSection == 0){
			holder.header.setText(leftMenu.get(section));
		}else{
			holder.header.setText(leftMenu.get(lastSection));
		}
		lastSection = section;
		return convertView;
	}
	
	public List<GoodsBean> getSelectedItem(){
		return selected;
	}
	
	public int getiBuyNum() {
		return iBuyNum;
	}

	public void setiBuyNum(int iBuyNum) {
		this.iBuyNum = iBuyNum;
	}

	public int getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(int totalMoney) {
		this.totalMoney = totalMoney;
	}

	class Viewholder{
		TextView goodsName;
		Button add;
		Button del;
		TextView price;
		TextView selectedNum;
		ImageView goodsImg;
	}
	
	class HeaderViewHolder{
		TextView header;
	}

	/**
	 * 局部刷新listview
	 * @param view
	 * @param section
	 * @param position
	 */
	public void updateView(View view, int section, int position) {
		if(view == null) {
			 return;
		}
		Viewholder holder = (Viewholder) view.getTag();													//从view中取得holder
		holder.selectedNum.setText(((GoodsBean)getItem(section, position)).getSelectedNum()+"");
		showGoodsOperation(section, position, holder);
	}

}
