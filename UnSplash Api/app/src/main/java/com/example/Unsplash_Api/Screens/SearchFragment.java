package com.example.Unsplash_Api.Screens;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.Unsplash_Api.R;
import com.example.Unsplash_Api.adapters.ImageAdapter;
import com.example.Unsplash_Api.api.ApiUtilities;
import com.example.Unsplash_Api.models.ImageModel;
import com.example.Unsplash_Api.models.SearchModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    SearchView searchView;
    private ArrayList<ImageModel> list;
    private ImageAdapter adapter;
    private ProgressDialog progressDialog;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    private int page=1;
    private int pagesize=30;
    private boolean isLoading;
    private boolean isLastPage;
    ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView=root.findViewById(R.id.recyclerview2);
        constraintLayout=root.findViewById(R.id.constraint_layout2);
        list=new ArrayList<>();
        adapter=new ImageAdapter(getContext(),list);
        gridLayoutManager=new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        searchView=root.findViewById(R.id.searchview1);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        if(isConnected()) {
            getData();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItem = gridLayoutManager.getChildCount();
                    int totalItem = gridLayoutManager.getItemCount();
                    int firstVisibleItemPos = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                    if (!isLoading && !isLastPage) {
                        if ((visibleItem + firstVisibleItemPos >= totalItem)
                                && firstVisibleItemPos >= 0 &&
                                totalItem >= pagesize) {
                            page++;
                            getData();
                        }
                    }
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    progressDialog.show();
                    searchData(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
        else {
            ImageView iv = new ImageView(getContext());
            iv.setImageResource(R.drawable.image_2);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            iv.setLayoutParams(lp);
            constraintLayout.addView(iv);
            searchView.setVisibility(SearchView.INVISIBLE);
        }
        return root;
    }

    private boolean isConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wificonn=mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconn=mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wificonn !=null && wificonn.isConnected() || mobileconn!=null && mobileconn.isConnected())
            return true;
        else
            return false;
    }



    public void searchData(String query){
        progressDialog.dismiss();
        ApiUtilities.getApiInterface().searchImages(query)
                .enqueue(new Callback<SearchModel>() {
                    @Override
                    public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                        list.clear();
                        list.addAll(response.body().getResults());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<SearchModel> call, Throwable t) {

                    }
                });
    }
    private void getData() {
        isLoading=true;
        ApiUtilities.getApiInterface().getImages(page,30)
                .enqueue(new Callback<List<ImageModel>>() {
                    @Override
                    public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                        if(response.body()!=null){
                            list.addAll(response.body());
                            adapter.notifyDataSetChanged();

                        }
                        isLoading=false;
                        progressDialog.dismiss();
                        if(list.size()>0){
                            isLastPage=list.size()<pagesize;
                        }else isLastPage=true;
                    }

                    @Override
                    public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                        Toast.makeText(getContext(),"Error In Fetching Images",Toast.LENGTH_SHORT);
                    }
                });
    }
}