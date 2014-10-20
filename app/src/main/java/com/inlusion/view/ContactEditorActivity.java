package com.inlusion.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.inlusion.controller.util.RoundedImageView;
import com.inlusion.maiavoip.R;

/**
 * Created by root on 14.10.16.
 */
public class ContactEditorActivity extends Activity {
    EditText nameBox;
    EditText numberBox;

    ImageButton confirmButton;
    ImageButton menuButton;
    ImageButton clearNameButton;
    ImageButton clearPhoneButton;
    ImageButton clearImageButton;

    View.OnTouchListener confirmButtonListener;
    View.OnTouchListener menuButtonListener;
    View.OnTouchListener clearNameListener;
    View.OnTouchListener clearNumberListener;
    View.OnTouchListener clearImageListener;

    RoundedImageView contactImageView;
    View.OnTouchListener contactImageListener;

    private static final int SELECT_PICTURE = 1;
    private static final int CROP_PICTURE = 2;
    public String selectedImagePath;
    private String filemanagerstring;
    public Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_editor);

        confirmButton = (ImageButton) findViewById(R.id.editcontact_accept_imagebutton);
        menuButton = (ImageButton) findViewById(R.id.editcontact_menu_imagebutton);

        nameBox = (EditText) findViewById(R.id.editcontact_name_edittext);
        numberBox = (EditText) findViewById(R.id.editcontact_phone_edittext);
        clearNameButton = (ImageButton) findViewById(R.id.editcontact_nameclear_imagebutton);
        clearPhoneButton = (ImageButton) findViewById(R.id.editcontact_phoneclear_imagebutton);
        clearImageButton = (ImageButton) findViewById(R.id.editcontact_imageclear_imagebutton);

        contactImageView = (RoundedImageView) findViewById(R.id.editcontact_contact_imageview);

        confirmButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
        menuButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
        clearNameButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
        clearPhoneButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
        clearImageButton.setEnabled(false);
        createListeners();
    }

    public void createListeners(){
        contactImageListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        contactImageView.setBackgroundResource(R.drawable.contact_blank_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        contactImageView.setBackgroundResource(R.drawable.contact_blank);
                        cropImage(selectedImageUri);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        clearNameListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clearNameButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        nameBox.setText("");
                        clearNameButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        clearNumberListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clearPhoneButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        numberBox.setText("");
                        clearPhoneButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        clearImageListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clearImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        clearContactImage();
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        confirmButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        confirmButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        confirmButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        menuButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        menuButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        menuButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                        break;
                    default:
                        return defaultResult;
                }
                return true;
            }
        };

        confirmButton.setOnTouchListener(confirmButtonListener);
        menuButton.setOnTouchListener(menuButtonListener);
        clearImageButton.setOnTouchListener(clearImageListener);
        clearNameButton.setOnTouchListener(clearNameListener);
        clearPhoneButton.setOnTouchListener(clearNumberListener);
        contactImageView.setOnTouchListener(contactImageListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("REQUEST="+requestCode+", RESULT="+resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                filemanagerstring = selectedImageUri.getPath();
                selectedImagePath = getPath(selectedImageUri);

//                //NOW WE HAVE OUR WANTED STRING
//                if(selectedImagePath!=null) {
//                    //System.out.println("selectedImagePath is the right one for you!");
//                    setContactImageByUri(selectedImageUri);
//                    contactImageView.setOval(true);
//                }

            }else if(requestCode == CROP_PICTURE){
                contactImageView.setOval(true);
                contactImageView.setPadding(0,0,0,0);
                setContactImageByUri(data.getData());
                System.out.println(data.getData());
            }
            clearImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
            clearImageButton.setEnabled(true);
        }
    }

    public String getPath(Uri uri) {
        String selectedImagePath;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        }else{
            selectedImagePath = null;
        }
        if(selectedImagePath == null){
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }

    public void setContactImageByUri(Uri uri){
        contactImageView.setImageURI(uri);
    }

    public void cropImage(Uri uri){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
// this will open all images in the Galery
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
// this defines the aspect ration
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
// this defines the output bitmap size
        cropIntent.putExtra("outputX", 300);
        cropIntent.putExtra("outputY", 300);
// true to return a Bitmap, false to directly save the cropped image
        //cropIntent.putExtra("return-data", true);
        //cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
        startActivityForResult(cropIntent, CROP_PICTURE);
//save output image in uri

    }

    public void clearContactImage(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Clear Image")
                .setMessage("Are you sure you want to clear the selected contact image?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactImageView.setOval(false);
                        contactImageView.setPadding(0,120,0,120);
                        contactImageView.setImageResource(R.drawable.contact);
                        clearImageButton.getDrawable().mutate().setColorFilter(Color.rgb(255, 255, 255), PorterDuff.Mode.MULTIPLY);
                        clearImageButton.setEnabled(false);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

}
