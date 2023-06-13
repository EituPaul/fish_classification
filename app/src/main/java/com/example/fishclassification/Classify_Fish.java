package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fishclassification.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Classify_Fish extends AppCompatActivity {

   // Button selectBtn,predictBtn,captureBtn;
    CardView selectBtncard,captureBtncard;
    TextView result; ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_fish);
       //Addedlottie animation
        LottieAnimationView animationView = findViewById(R.id.lottie_animation);
        LottieAnimationView gallery = findViewById(R.id.lottie_animation_selectimg);
        LottieAnimationView capture = findViewById(R.id.lottie_animation_predictimg);
        LottieAnimationView resultanim = findViewById(R.id.lottie_animation_resultimg);
        animationView.playAnimation();
        animationView.loop(true);
        gallery.playAnimation();
        gallery.loop(true);
        capture.playAnimation();
        capture.loop(true);
        resultanim.playAnimation();
        resultanim.loop(true);
        // end Addedlottie animation
        //permission
        getPermission();
       // selectBtn = findViewById(R.id.selectbtn);
       // predictBtn = findViewById(R.id.predictBtn);
       // captureBtn = findViewById(R.id.captureBtn);
        selectBtncard = findViewById(R.id.selectbtn);
        captureBtncard = findViewById(R.id.captureBtn);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        selectBtncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
        captureBtncard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);
            }
        });
    }//end oncreate
    public void classifyImage(Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(Classify_Fish.this);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[224 * 224];
            image.getPixels(intValues,0,image.getWidth(),0,0,image.getWidth(),image.getHeight());
            int pixle = 0;
            for(int i = 0;i< 224;i++){
                for (int j=0; j< 224;j++){
                    int val = intValues[pixle++];//RGB
                    byteBuffer.putFloat(((val >> 16)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8)& 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // result.setText(labelfish[getMax(outputFeature0.getFloatArray())]+"");

            float [] probability = outputFeature0.getFloatArray();
            int max =0;
            float maxprob = 0;
            for (int i =0; i<probability.length;i++){
                if(probability[i] > maxprob){
                    maxprob = probability[i];
                    max =i;
                }
            }

            String[] classes = {"Boal" , "Koi" ,"Ilish"," Katla" ,"Pabda" ,"Pangas" ,"Rui" ,"Taki" ,"Telapiya"};
            result.setText(classes[max]+" with accuracy "+maxprob);



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }


    int getMax(float[] arr){
        int max=0;
        for(int i=0;i<arr.length;i++){
            if(arr[i]>arr[max])max=i;
        }
        return max;
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Classify_Fish.this,new String[]{Manifest.permission.CAMERA},11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode ==11)
        {
            if(grantResults.length>0)
            {
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==10){
            if(data!=null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);

                    imageView.setImageBitmap(bitmap);
                    bitmap = Bitmap.createScaledBitmap(bitmap,224,224,false);
                    classifyImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        else if(requestCode==12){
            //ekhane if condition added by me, try catch also added by me  karon back button e click korle app off hoy
            if(data!=null) {
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
                    classifyImage(bitmap);
                }catch (NullPointerException e){
                    e.printStackTrace();

                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}