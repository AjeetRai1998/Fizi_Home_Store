package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.nio.file.AtomicMoveNotSupportedException;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.MenuAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityRestaurantMenuBinding;

public class RestaurantMenuActivity extends AppCompatActivity {

    ActivityRestaurantMenuBinding binding;
    private ImageView image;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRestaurantMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // handle back

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //load menu list

        binding.menuList.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        MenuAdapter adapter=new MenuAdapter();
        adapter.findItemPosition(new MenuAdapter.GetPosition() {
            @Override
            public void getPos(View view, int position, MenuAdapter.MyHolder holder) {
                initRecyclerItem(holder.itemView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);
                    }
                    else
                    {
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"select file"),1);

                    }
                }
            }
        });
        binding.menuList.setAdapter(adapter);

    }
        private void initRecyclerItem(View itemView) {
        image=itemView.findViewById(R.id.service_image);
        layout=itemView.findViewById(R.id.add_images_layout);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null)
        {
            image.setImageURI(data.getData());
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select file"),1);
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1001);

        }
    }
}