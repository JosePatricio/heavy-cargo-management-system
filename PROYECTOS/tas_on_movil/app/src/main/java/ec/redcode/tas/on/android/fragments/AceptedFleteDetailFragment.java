package ec.redcode.tas.on.android.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.redcode.tas.on.android.adapters.conductor.ImagentemAdapterRV;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.services.ImageService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class AceptedFleteDetailFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST = 112;
    private static final int GALLERY_PERMISSION_REQUEST = 404;
    protected static final int REQUEST_TAKE_PHOTO = 1;
    protected static final int GALLERY_REQUEST = 2;
    protected static final int CALL_PERMISSION_REQUEST = 99;
    protected static String PHONE_NUMBER = "";

    protected FleteDTO flete;
    protected List<Bitmap> nuevasFotos = new ArrayList<>();
    protected ImageView buttonTakePicture;
    protected GalleryPhoto galleryPhoto;
    protected AlertDialog dialog;
    private String mCurrentPhotoPath;
    private ImagentemAdapterRV adapter;

    public AceptedFleteDetailFragment() { }

    protected static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Los objetos de la librería que nos ayudarán en la tarea
        galleryPhoto = new GalleryPhoto(getContext());

        // Array con las opciones para el diálogo que se abrirá al pulsar el botón "PIC"
        final String[] items = new String[]{"Cámara", "Galeria"};

        // Creamos el diálogo
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Seleccione entrada:");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Opción Cámara
                    callCameraApp();
                    dialog.cancel();
                } else {
                    // Opción Galería
                    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(getContext(), PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, GALLERY_PERMISSION_REQUEST);
                        if (hasPermissions(getContext(), PERMISSIONS)) uploadPicture();
                    } else {
                        uploadPicture();
                    }

                }
            }
        });

        dialog = builder.create();
    }

    protected void setEventsPicture(RecyclerView imagenesRecyclerView, View button) {
        // Buscamos el botón por su ID y creamos una referencia al mismo
        this.buttonTakePicture = (ImageView) button;

        this.adapter = new ImagentemAdapterRV(getContext(), nuevasFotos, true);
        asignarReciclerViewHorizontal(imagenesRecyclerView, this.adapter);

        //Dialogo para tomar una foto o subir una imagen de la galeria
        this.buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    protected void asignarReciclerViewHorizontal(RecyclerView recyclerView, ImagentemAdapterRV adapter){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    protected void setFlete(FleteDTO flete) {
        this.flete = flete;
    }

    protected void askForPicture(int state, final List<Bitmap> fotos, final ImagentemAdapterRV adapater, final LinearLayout loadingPhoto) {
        ImageService service = new ImageService();
        Observable<List<String>> observable = service.getImage(flete.getOffer().getIdOferta(), state);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<String>>() {
                    @Override
                    public void onNext(List<String> imagenes) {
                        try {
                            for(String imagen : imagenes){
                                byte[] decodedString = Base64.decode(imagen, Base64.DEFAULT);
                                Bitmap fotoBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                fotos.add(fotoBitmap);
                                adapater.notifyDataSetChanged();
                                loadingPhoto.setVisibility(View.GONE);
                            }

                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    protected void finish() {
        getActivity().finish();
    }

    protected boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private void callCameraApp() {
        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) getContext(), PERMISSIONS, CAMERA_PERMISSION_REQUEST);
            if (hasPermissions(getContext(), PERMISSIONS)) takePicture();
        } else {
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    permissionDeniedMessage();
                }
                break;
            }
            case GALLERY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                } else {
                    permissionDeniedMessage();
                }
                break;
            }
            case CALL_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall(PHONE_NUMBER);
                } else {
                    permissionDeniedMessage();
                }
                break;
            }
        }
    }

    private void permissionDeniedMessage(){
        Toast.makeText(getContext(),
                "No se cuenta con los permisos para acceder a esta funcionalidad",
                Toast.LENGTH_LONG).show();
    }

    public void makePhoneCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            getContext().startActivity(intent);
        }
    }


    private void takePicture(){
        if(!checkCameraHardware(getContext())){
            Toast.makeText(getContext(), "El sistema no cuenta con una cámara", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = createImageFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } catch (IOException e) {
            Toast.makeText(getContext(), "No se pudo tomar la foto", Toast.LENGTH_LONG).show();
        }
    }

    private void uploadPicture(){
        startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TASON_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    protected static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //metodo que actua despues de tomar una foto o subir una foto de la galeria
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                // Obtenemos el path de la imagen que hemos tomado
                String photoPath = mCurrentPhotoPath;
                try {
                    // Creamos un bitmap a partir de la imagen
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    //Corregimos inclinacion imagen
                    bitmap = rotateBitmap(bitmap, 90);
                    // Aplicamos el Bitmap al ImageView
                    nuevasFotos.add(bitmap);
                    adapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getContext(), "Error al cargar la foto", Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == GALLERY_REQUEST) {
                // Creamos una ruta en formato Uri para los datos correspondientes a la imagen
                Uri uri = data.getData();
                // Asignamos esa ruta al objeto de la librería
                galleryPhoto.setPhotoUri(uri);
                // Obtenemos la ruta completa para acceder a la imagen
                String photoPath = galleryPhoto.getPath();
                try {
                    // Seguimos el mismo proceso que para la cámara
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    nuevasFotos.add(bitmap);
                    adapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getContext(), "No se ha encontrado el archivo", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Toast.makeText(getContext(), "No se ha tomado la foto", Toast.LENGTH_LONG).show();
            }
        }

    }
}
