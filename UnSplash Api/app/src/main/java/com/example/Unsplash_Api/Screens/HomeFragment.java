package com.example.Unsplash_Api.Screens;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.Unsplash_Api.R;
import com.example.Unsplash_Api.adapters.ImageAdapter;
import com.example.Unsplash_Api.api.ApiUtilities;
import com.example.Unsplash_Api.models.ImageModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

   private RecyclerView recyclerView;
   private ArrayList<ImageModel> list;
   private GridLayoutManager gridLayoutManager;
   private ImageAdapter adapter;
   private ProgressDialog progressDialog;
   private int page=1;
   private int pagesize=30;
   private boolean isLoading;
   private boolean isLastPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=root.findViewById(R.id.recyclerview1);
        list=new ArrayList<>();
        adapter=new ImageAdapter(getContext(),list);
        gridLayoutManager=new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading.....");
        progressDialog.setCancelable(false);
        getData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItem=gridLayoutManager.getChildCount();
                int totalItem=gridLayoutManager.getItemCount();
                int firstVisibleItemPos= gridLayoutManager.findFirstCompletelyVisibleItemPosition();

                if(!isLoading && !isLastPage){
                    if((visibleItem+firstVisibleItemPos>=totalItem)
                            && firstVisibleItemPos>= 0 &&
                            totalItem>=pagesize)
                    {
                        page++;
                        getData();
                    }
                }
            }
        });
        return  root;
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