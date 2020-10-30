Instalación 


1) yarn

2) cd android && .\gradlew clean     


Para el cambio de idioma del seleccionador de imágenes modificar la variable DEFAULT_OPTIONS del archivo node_modules/react-native-image-picker/src/index.ts   a

const DEFAULT_OPTIONS: ImagePickerOptions = {
  title: 'Seleccione entrada',
  cancelButtonTitle: 'Cancelar',
  takePhotoButtonTitle: 'Cámara',
  chooseFromLibraryButtonTitle: 'Galería',
  quality: 1.0,
  allowsEditing: false,
  permissionDenied: {
    title: 'Permiso negado',
    text:
      'Para tomar fotos con tu cámara y escoger imágenes de tu galería.',
    reTryTitle: 'Re intentar',
    okTitle: "Estoy seguro",
  },
  tintColor: 'blue',
};




3) cd .. && react-native run-android






Generación del apk

1) Ejecutar el siguiente comando
react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res

2) Desde Android Studio Generar el Apk