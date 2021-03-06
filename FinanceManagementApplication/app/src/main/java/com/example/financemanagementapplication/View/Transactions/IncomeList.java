package com.example.financemanagementapplication.View.Transactions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.financemanagementapplication.R;
import com.example.financemanagementapplication.View.Transactions.transactionAdapter.CustomAdapter;
import com.example.financemanagementapplication.model.database.entity.Income;
import com.example.financemanagementapplication.model.viewmodel.IncomeCategoryVM;
import com.example.financemanagementapplication.model.viewmodel.IncomeVM;
import com.example.financemanagementapplication.model.viewmodel.SpendingCategoryVM;
import com.example.financemanagementapplication.tool.DateTool;

public class IncomeList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean edit;
    private boolean switchLeft;
    private String date;
    private CustomAdapter adapter;
    private IncomeVM incomeVM;
    private IncomeCategoryVM cateIvm;
    private SpendingCategoryVM cateSvm;
    private ListView list;
    private Boolean alltime = false;
    public IncomeList() {
        // Required empty public constructor
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public IncomeList(boolean edit, Boolean switchLeft, String date) {
        this.edit = edit;
        this.switchLeft = switchLeft;
        this.date = date;
    }

    public IncomeList(boolean edit, Boolean sl, String date,Boolean allTime) {
        this.edit = edit;
        this.switchLeft = sl;
        this.date = date;
        this.alltime = allTime;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeList.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeList newInstance(String param1, String param2) {
        IncomeList fragment = new IncomeList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        incomeVM = new ViewModelProvider(requireActivity()).get(IncomeVM.class);
        cateIvm = new ViewModelProvider(requireActivity()).get(IncomeCategoryVM.class);
        cateSvm = new ViewModelProvider(requireActivity()).get(SpendingCategoryVM.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("START","-------------------------------------------------------------");
        View view = inflater.inflate(R.layout.fragment_transactions_list, container, false);
        list = view.findViewById(R.id.trans_list_ele);
        FrameLayout l = (FrameLayout) view.findViewById(R.id.frame_layout);
        if(date.equals(DateTool.today()))
        {
            l.setBackgroundColor(view.getResources().getColor(R.color.white));
        }
        else{
            l.setBackgroundColor(view.getResources().getColor(R.color.ordinary_blur));
        }
        if (!alltime)
        {incomeVM.getIncomesByDate(date).observe(getViewLifecycleOwner(), incomes -> {
            adapter = new CustomAdapter(getContext(), R.layout.ele_list_finance_fragment,incomes,edit,cateIvm,cateSvm,getViewLifecycleOwner(),switchLeft);
            list.setAdapter(adapter);
        });}
        else
        {
            incomeVM.getIncomes().observe(getViewLifecycleOwner(), incomes -> {
                adapter = new CustomAdapter(getContext(), R.layout.ele_list_finance_fragment,Income.sortByDate(incomes),edit,cateIvm,cateSvm,getViewLifecycleOwner(),switchLeft,alltime);
                list.setAdapter(adapter);
            });
        }
        list.setScrollingCacheEnabled(true);
        return view;
    }

    public void update(boolean editMode,Boolean alltime)
    {
        if(alltime){
        incomeVM.getIncomes().observe(getViewLifecycleOwner(), incomes -> {
            adapter = new CustomAdapter(getContext(), R.layout.ele_list_finance_fragment, Income.sortByDate(incomes),edit,cateIvm,cateSvm,getViewLifecycleOwner(),switchLeft,alltime);
            adapter.setEdit(editMode);
            list.setAdapter(adapter);
        });}
        else{
            incomeVM.getIncomesByDate(date).observe(getViewLifecycleOwner(), incomes -> {
                adapter = new CustomAdapter(getContext(), R.layout.ele_list_finance_fragment,incomes,edit,cateIvm,cateSvm,getViewLifecycleOwner(),switchLeft);
                adapter.setEdit(editMode);
                list.setAdapter(adapter);
            });
        }
    }
    public void setSwitchLeft(boolean switchLeft)
    {
        this.adapter.setSwitchLeft(switchLeft);
    }
}
