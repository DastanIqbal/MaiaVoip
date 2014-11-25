package com.inlusion.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inlusion.controller.util.ContactUtils;
import com.inlusion.controller.util.RoundedImageView;
import com.inlusion.maiavoip.R;

/**
 * Created by Linas Martusevicius on 14.10.16.
 * <p/>
 * ContactEditorActivity logic implementation.
 */
public class ContactEditorActivity extends Activity {
    ContactUtils cu;

    EditText nameBox;
    EditText numberBox;

    ImageButton confirmButton;
    ImageButton menuButton;
    ImageButton clearNameButton;
    ImageButton clearPhoneButton;
    ImageButton clearImageButton;

    PopupWindow menuPopup;

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

    int paddingTop;
    int paddingRight;
    int paddingBottom;
    int paddingLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_editor);

        cu = ContactUtils.getInstance(this);

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

        paddingTop = contactImageView.getPaddingTop();
        paddingRight = contactImageView.getPaddingRight();
        paddingBottom = contactImageView.getPaddingBottom();
        paddingLeft = contactImageView.getPaddingLeft();

        setUp();

        menuPopup = new PopupWindow(this);
        createPopupMenu();
        createListeners();
    }

    /**
     * Creates and sets all listeners for touch events within the ContactEditorActivity.
     */
    public void createListeners() {
        contactImageListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean defaultResult = v.onTouchEvent(event);
                switch (event.getAction()) {
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
                switch (event.getAction()) {
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
                switch (event.getAction()) {
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
                switch (event.getAction()) {
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
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        confirmButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        confirmButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                        Bitmap bitmap = ((BitmapDrawable) contactImageView.getDrawable()).getBitmap();
                        saveContact(nameBox.getText().toString(), numberBox.getText().toString(), bitmap);
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
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        menuButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_pressed), PorterDuff.Mode.MULTIPLY);
                        break;
                    case MotionEvent.ACTION_UP:
                        menuButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
                        menuPopup.showAsDropDown(menuButton);
                        menuPopup.update(0, 0, menuPopup.getWidth(), menuPopup.getHeight());
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

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                filemanagerstring = selectedImageUri.getPath();
                selectedImagePath = getPath(selectedImageUri);
            } else if (requestCode == CROP_PICTURE) {
                contactImageView.setOval(true);
                contactImageView.setPadding(0, 0, 0, 0);
                setContactImageByUri(data.getData());
                System.out.println(data.getData());
            }
            clearImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.control_grey_idle), PorterDuff.Mode.MULTIPLY);
            clearImageButton.setEnabled(true);
        } else {
            //contactImageView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        }
    }

    /**
     * Retrieves the path of a contact's image from the device's database.
     *
     * @param uri the URI of the image which was selected.
     * @return the absolute path of the image which was selected.
     */
    public String getPath(Uri uri) {
        String selectedImagePath;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
        } else {
            selectedImagePath = null;
        }
        if (selectedImagePath == null) {
            selectedImagePath = uri.getPath();
        }
        return selectedImagePath;
    }

    /**
     * Sets the contacts image from a URI.
     *
     * @param uri a String representation of the URI of an image to be set as the contact's image.
     */
    public void setContactImageByUri(Uri uri) {
        contactImageView.setImageURI(uri);
    }

    /**
     * Starts an intent for cropping the selected contact image.
     *
     * @param uri the URI of the image which was selected.
     */
    public void cropImage(Uri uri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 300);
        cropIntent.putExtra("outputY", 300);
        startActivityForResult(cropIntent, CROP_PICTURE);
    }

    /**
     * Clears the contact's selected image.
     */
    public void clearContactImage() {
        Drawable icon = getApplicationContext().getResources().getDrawable(android.R.drawable.ic_dialog_alert);
        icon.mutate().setColorFilter(getResources().getColor(R.color.maia_pink), PorterDuff.Mode.MULTIPLY);
        AlertDialog ad = new AlertDialog.Builder(this)
                .setIcon(icon)
                .setTitle("Clear Image")
                .setMessage("Are you sure you want to clear the selected contact image?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        contactImageView.setOval(false);
                        contactImageView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                        contactImageView.setImageResource(R.drawable.contact);
                        clearImageButton.getDrawable().mutate().setColorFilter(getResources().getColor(R.color.white_FFFFFF), PorterDuff.Mode.MULTIPLY);
                        clearImageButton.setEnabled(false);
                    }

                })
                .setNegativeButton("No", null)
                .show();
        colorAlertDialog(ad);
    }

    /**
     * Stylizes the alert dialog.
     *
     * @param dialog the dialog to be stylized.
     */
    public static void colorAlertDialog(AlertDialog dialog) {
        try {
            Resources resources = dialog.getContext().getResources();
            int color = resources.getColor(R.color.maia_pink); // your color here

            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(color); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color); // change divider color
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates a popup menu.
     */
    public void createPopupMenu() {
        View view;
        LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.contact_menu, null);
        menuPopup.setContentView(view);
    }

    /**
     * WARNING - BUGGY, NOT YET IMPLEMENTED!
     *
     * @param name
     * @param number
     * @param image
     */
    public void saveContact(String name, String number, Bitmap image) {
        cu.saveContact(name, number, image);
    }

    /**
     * Sets initial data for the activity if a contact is being edited.
     */
    public void setUp() {
        try {
            String name = getIntent().getStringExtra("name");
            String number = getIntent().getStringExtra("number");
            Bitmap image = (Bitmap) getIntent().getExtras().get("image");

            if (name != null) {
                nameBox.setText(name);
            }
            if (number != null) {
                numberBox.setText(number);
            }
            if (image != null) {
                contactImageView.setImageBitmap(image);
                contactImageView.setOval(true);
                contactImageView.setPadding(0, 0, 0, 0);
            }
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException in contacteditor activity; setting image null. Expected and ignored.");
        }
    }
}
