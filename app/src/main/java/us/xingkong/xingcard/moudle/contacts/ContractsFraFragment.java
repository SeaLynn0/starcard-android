package us.xingkong.xingcard.moudle.contacts;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import us.xingkong.xingcard.R;
import us.xingkong.xingcard.adapter.expandableRecyclerviewAdapter.ImpExpandableAdapter;
import us.xingkong.xingcard.base.BaseFragment;
import us.xingkong.xingcard.bean.Contacts;
import us.xingkong.xingcard.bean.ExpandGroup;
import us.xingkong.xingcard.utils.SearchUtils;
import xingkong.us.expandablerecycleradapter.adapter.BaseExpandableAdapter;

import static us.xingkong.xingcard.base.Constants.STAR_ID;

/**
 * @author hugeterry(http://hugeterry.cn)
 */
public class ContractsFraFragment extends BaseFragment<ContractsFraContract.Presenter> implements ContractsFraContract.View {

    @BindView(R.id.sv_contracts)
    SearchView mSearchView;
    @BindView(R.id.rv_contracts)
    RecyclerView mRecyclerView;

    ImpExpandableAdapter mImpExpandableAdapter;
    List<ExpandGroup> mExpandGroups = new ArrayList<>();

    private String mStarIDString;

    @Override
    protected ContractsFraContract.Presenter createPresenter() {
        return new ContractsFraPresenter(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        mStarIDString = bundle.getString(STAR_ID);
        Log.i("hugeterry", "prepareData: " + mStarIDString);
        if (mStarIDString != null) {
            mPresenter.getContactsList(mStarIDString);
        }
    }

    @Override
    protected void initView(View rootView) {
        setToolbarTitle("联系人");
        mSearchView.clearFocus();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }


    @Override
    protected void initEvent() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    List<ExpandGroup> mExpandGroupsRearch = mExpandGroups;
                    List<ExpandGroup> expandGroups = SearchUtils.filterData(newText, mExpandGroupsRearch);
                    mImpExpandableAdapter.updateData(expandGroups);
                    mImpExpandableAdapter.expandAllParents();
                } else {
                    mImpExpandableAdapter.reUpdateData(mExpandGroups);
                }
                return false;
            }
        });
    }

    public void initRecyclerView(Contacts contacts) {
        mExpandGroups.addAll(contacts.getDepartments());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mImpExpandableAdapter = new ImpExpandableAdapter(getActivity(), mExpandGroups);
        mRecyclerView.setAdapter(mImpExpandableAdapter);
        mImpExpandableAdapter.setExpandCollapseListener(new BaseExpandableAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {

            }

            @Override
            public void onListItemCollapsed(int position) {

            }
        });
    }
}