package com.example.criminallntent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/*Crime实现了每个数据项，CrimeLab实现了在整个应用中作为一个数组池存放这些数据。
 CrimeListFragment要用在Fragement中用RecycleView来实现这个展示各个数据项的列表视图
 RecycleView通过adapter创建和配置各个ViewHolder，每个ViewHolder连接一个View显示
 简单分为4步骤：
 1.配置reCycleView视图，添加xml，并且将reCycleView视图和Fragment相关联，而reCycleView会创建一个个ViewHolder
 2.定义ViewHolder，ViewHolder是一种容器，用来容纳一些View
 3.重写adapter的相关方法，adapter用来从模型层获取数据并提供给RecycleView显示，所以需要实现创建ViewHolder，并且绑定每个ViewHolder
 到模型层对应的数据
 4.定义好后，创建Adapter并且设置给recycleView
 */
public class CrimeListFragment extends Fragment {
    //创建RecycleView和adapter的对象
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Fragment的onCreateView类似activity中的oncreate，第一行惯例引入界面
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        //步骤1的配置RecyclerView视图，将视图与fragment相关联，寻找到布局文件，创建完成后转交给LayoutManager对象
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //

        //
        updateUI();
        return view;
    }
    /*步骤2：ViewHolder类的实现，ViewHolder容纳着各种VIew，现在的列表项是由两个TextView和一个CheckBox组成的
     ViewHolder容器提前将各种View寻找到并保存，所以需要个ViewHolder
     private class CrimeHolder extends RecyclerView.ViewHolder{} 这是原方法，新的版本把这里增加了监听接口。
     java不允许多重继承，但可以额外实现接口，这里就是只继承了ViewHolder然后同时实现了监听类的接口
    所以现在ViewHolder类具有监听能力，我们在里面增加个OnClick方法
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        //新建列表项中所需的各种view
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView){
            super(itemView);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }
        //实现绑定的方法，事实上这一步应该是又adapter来完成，将crime与holder绑定。这里直接在holder立马提供方法，在
        private Crime mCrime;
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
        //实现监听方法，按键点击后启动Crime列表项对应的元素的activity，是用的显式intent，同时通过EXTRA传入mCrime信息，指明哪个crime
        // 注意getActivity是fragment的一种方法，用来获取其所依附的activity
        @Override
        public void onClick(View v){
            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            startActivity(intent);
        }
    }

    //步骤3：Adapter的相关实现，要创建ViewHolder并且与数据绑定，封装从CrimeLab获取的crime
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        //这里使用的数据是前面写好的Crime的数组类，其构造方法获取一个Lis<Crime>对象
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }
        //RecyclerView需要视图对象时会找它的adapter，首先，调用getItemCount方法询问数组列表有多少对象
        @Override
        public int getItemCount(){
            return mCrimes.size();
        }

        //然后，RecyclerView调用adapter的createViewHolder方法创建ViewHolder以及ViewHolder要显示的视图
        //
        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            //创建所需的View视图
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            //将视图封装到Viewholder中
            return new CrimeHolder(view);
        }
        //最后，RecyclerView会调用onBindViewHolder方法，完成绑定，也就是数据填充
        // 这个方法会将ViewHolder的View视图和模型层的数据绑定，mCrime是Crime数组，得到索引位置后取出数据赋值给crime
        //然后通过发送cirme标题给ViewHolder视图的TextView视图就完成了Cirme和View视图的绑定.
        //具体实现在holder类中
        @Override
        public void onBindViewHolder(CrimeHolder holder,int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }


    }
    //步骤5：实例化前面定义好的adapter（包括从CrimeLab获取数据，然后传给adapter），设置给CrimeRecyclerView
    //这里要考虑，如果不是第一次实例化，比如是Crime被销毁后要onResume，那么就不用new要用其他方法
    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if(mAdapter == null){
            //正常第一次实例化
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
    } else {
            //列表项打开后的activity被销毁后更新内容
            mAdapter.notifyDataSetChanged();
        }
    }
    //当crimeFragment修改信息回退后，需要更新UI，所以这里要覆盖onResume
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
}
