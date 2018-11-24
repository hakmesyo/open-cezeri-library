/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.ic_camera;

import cezeri.utils.FactoryUtils;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

/**
 *
 * @author HP-pc
 */
public interface IC_CameraDLL extends Library {

    public enum IMG_FILETYPE {

        FILETYPE_BMP,
        FILETYPE_JPEG,
    }

    public enum COLORFORMAT {

        Y800,
        RGB24,
        RGB32,
        UYVY,
        Y16,
        NONE,
    }

    public enum VIDEO_PROPERTY {

        PROP_VID_BRIGHTNESS,
        PROP_VID_CONTRAST,
        PROP_VID_HUE,
        PROP_VID_SATURATION,
        PROP_VID_SHARPNESS,
        PROP_VID_GAMMA,
        PROP_VID_COLORENABLE,
        PROP_VID_WHITEBALANCE,
        PROP_VID_BLACKLIGHTCOMPENSATION,
        PROP_VID_GAIN,

    }

    public enum CAMERA_PROPERTY {

        PROP_CAM_PAN,
        PROP_CAM_TILT,
        PROP_CAM_ROLL,
        PROP_CAM_ZOOM,
        PROP_CAM_EXPOSURE,
        PROP_CAM_IRIS,
        PROP_CAM_FOCUS,
    }

    public enum FRAMEFILTER_PARAM_TYPE {

        eParamLong,
        eParamBoolean,
        eParamFloat,
        eParamString,
        eParamData
    }

    public enum PROPERTY_INTERFACE_TYPE {

        ePropertyRange,
        ePropertyAbsoluteValue,
        ePropertySwitch,
        ePropertyButton,
        ePropertyMapStrings,
        ePropertyUnknown
    }

//    IC_CameraDLL INSTANCE = (IC_CameraDLL) Native.loadLibrary(FactoryUtils.getDefaultDirectory()+"\\dll\\tisgrabber.dll", IC_CameraDLL.class);
    IC_CameraDLL INSTANCE = (IC_CameraDLL) Native.loadLibrary(FactoryUtils.getDefaultDirectory()+"\\dll\\tisgrabber_x64.dll", IC_CameraDLL.class);

    int IC_InitLibrary(String key);                                                                         //int AC IC_InitLibrary( char* szLicenseKey );///<Initialize the library.

    void IC_CloseLibrary();                                                                                 //void AC IC_CloseLibrary(); ///< Closes the library, cleans up memory. 

    int IC_CreateGrabber();                                                                                 //HGRABBER AC IC_CreateGrabber();///<Create a new grabber handle

    void IC_ReleaseGrabber(IntByReference hgrabber);                                                        //void AC IC_ReleaseGrabber( HGRABBER *hGrabber ); ///< Releas an HGRABBER object.

    int IC_OpenVideoCaptureDevice(int hgrabber, String deviceName);                                          //int AC IC_OpenVideoCaptureDevice( HGRABBER hGrabber, char *szDeviceName ); ///< Opens a video capture device.

    void IC_CloseVideoCaptureDevice(int hgrabber);                                                          //void AC IC_CloseVideoCaptureDevice( HGRABBER hGrabber ); ///<Closes a video capture device.

    String IC_GetDeviceName(int hgrabber);                                                                  //char* AC IC_GetDeviceName(HGRABBER hGrabber ); ///<Returns the name of the current video capture device.

    int IC_GetVideoFormatWidth(int hgrabber);                                                               //int AC IC_GetVideoFormatWidth( HGRABBER hGrabber); ///<Returns the width of the video format.

    int IC_GetVideoFormatHeight(int hgrabber);                                                              //int AC IC_GetVideoFormatHeight( HGRABBER hGrabber);///<returns the height of the video format.

    int IC_SetFormat(int hgrabber, int colorformat);                                                        //int AC IC_SetFormat( HGRABBER hGrabber, COLORFORMAT format ); ///< Sets the color format of the sink.

    int IC_GetFormat(int hgrabber);                                                                         //COLORFORMAT AC IC_GetFormat( HGRABBER hGrabber ); ///<Returns the current color format of the sink.

    int IC_SetVideoFormat(int hgrabber, String sizeFormat);                                                 //int AC IC_SetVideoFormat( HGRABBER hGrabber, char *szFormat ); ///<Sets the video format.

    int IC_SetVideoNorm(int hgrabber, String sizeNorm);                                                     //int AC IC_SetVideoNorm( HGRABBER hGrabber, char *szNorm ); ///<Set the video norm.

    int IC_SetInputChannel(int hgrabber, String sizeChannel);                                               //int AC IC_SetInputChannel( HGRABBER hGrabber, char *szChannel ); ///<Sets an input channel.

    int IC_StartLive(int hgrabber, int iShow);                                                              //int AC IC_StartLive( HGRABBER hGrabber, int iShow ); ///<Starts the live video.

    int IC_PrepareLive(int hgrabber, int iShow);                                                            //int AC IC_PrepareLive( HGRABBER hGrabber, int iShow); ///<Prepare the grabber for starting the live video.

    int IC_SuspendLive(int hgrabber);                                                                       //int AC IC_SuspendLive(HGRABBER hGrabber); ///<Suspends an image stream and puts it into prepared state.

    int IC_IsLive(int hgrabber);                                                                            //int AC IC_IsLive( HGRABBER hGrabber );

    void IC_StopLive(int hgrabber);                                                                         //void AC IC_StopLive( HGRABBER hGrabber ); ///<Stops the live video.

    int IC_IsCameraPropertyAvailable(int hgrabber, int camProperty);                                        //int AC IC_IsCameraPropertyAvailable( HGRABBER hGrabber, CAMERA_PROPERTY eProperty ); ///< Check whether a camera property is available.

    int IC_SetCameraProperty(int hgrabber, int camProperty, long value);                                    //int AC IC_SetCameraProperty( HGRABBER hGrabber, CAMERA_PROPERTY eProperty, long lValue ); ///< Set a camera property.

    int IC_CameraPropertyGetRange(int hgrabber, int camProperty, LongByReference lMin, LongByReference lMax);//int AC IC_CameraPropertyGetRange( HGRABBER hGrabber, CAMERA_PROPERTY eProperty, long *lMin, long *lMax); ///<Get the minimum and maximum value of a camera property

    int IC_GetCameraProperty(int hgrabber, int camProperty, LongByReference val);                           //int AC IC_GetCameraProperty( HGRABBER hGrabber, CAMERA_PROPERTY eProperty, long *lValue);  ///< Get a camera property's value.

    int IC_EnableAutoCameraProperty(int hgrabber, int property, int onOff);                                 //int AC IC_EnableAutoCameraProperty( HGRABBER hGrabber, int iProperty, int iOnOff ); ///<Enables or disables property automation.

    int IC_IsCameraPropertyAutoAvailable(int hgrabber, int camProperty);                                    //int AC IC_IsCameraPropertyAutoAvailable( HGRABBER hGrabber, CAMERA_PROPERTY iProperty ); ///<Check whether automation for a camera property is available.

    int IC_GetAutoCameraProperty(int hgrabber, int property, IntByReference onOff);                         //int AC IC_GetAutoCameraProperty( HGRABBER hGrabber, int iProperty, int *iOnOff ); ///<Retrieve whether automatic is enabled for the specifield camera property.

    int IC_IsVideoPropertyAvailable(int hgrabber, int vidProperty);                                         //int AC IC_IsVideoPropertyAvailable( HGRABBER hGrabber, VIDEO_PROPERTY eProperty ); ///<Check whether the specified video property is available. 

    int IC_VideoPropertyGetRange(int hgrabber, int vidProperty, LongByReference lMin, LongByReference lMax);//int AC IC_VideoPropertyGetRange( HGRABBER hGrabber, VIDEO_PROPERTY eProperty, long *lMin, long *lMax); ///<Retrieve the lower and upper limit of a video property.

    int IC_GetVideoProperty(int hgrabber, int vidProperty, LongByReference value);                          //int AC IC_GetVideoProperty( HGRABBER hGrabber, VIDEO_PROPERTY eProperty, long *lValue ); ///< Retrieve the the current value of the specified video property.

    int IC_IsVideoPropertyAutoAvailable(int hgrabber, int vidProperty);                                     //int AC IC_IsVideoPropertyAutoAvailable( HGRABBER hGrabber, VIDEO_PROPERTY eProperty ); ///<Check whether the specified video property supports automation.

    int IC_GetAutoVideoProperty(int hgrabber, int property, IntByReference onOff);                          //int AC IC_GetAutoVideoProperty( HGRABBER hGrabber, int iProperty, int *iOnOff ); ///<Get the automation state of a video property.

    int IC_SetVideoProperty(int hgrabber, int property, long value);                                        //int AC IC_SetVideoProperty( HGRABBER hGrabber, VIDEO_PROPERTY eProperty, long lValue ); ///<Set a video property.

    int IC_EnableAutoVideoProperty(int hgrabber, int prop, int onOff);                                      //int AC IC_EnableAutoVideoProperty( HGRABBER hGrabber, int iProperty, int iOnOff ); ///< Switch automatition for a video property,

    int IC_GetImageDescription(int hgrabber, LongByReference w, LongByReference h, IntByReference bpp, IntByReference clFormat);//int AC IC_GetImageDescription( HGRABBER hGrabber, long *lWidth, long *lHeight, int *iBitsPerPixel, COLORFORMAT *format );///<Retrieve the properties of the current video format and sink typ.

    int IC_SnapImage(int hgrabber, int val);                                                                //int AC IC_SnapImage( HGRABBER hGrabber, int iTimeOutMillisek); ///<Snaps an image from the live stream. 

    int IC_SaveImage(int hgrabbber, String path, int ft, long q);                                           //int AC IC_SaveImage( HGRABBER hGrabber, char *szFileName, IMG_FILETYPE ft, long quality ); ///< Saves an image to a file.

    Pointer IC_GetImagePtr(int hgrabber);                                                        //unsigned char* AC IC_GetImagePtr( HGRABBER hGrabber ); ///< Retuns a pointer to the image data

    int IC_SetHWnd(int hgrabber, int hwnd);                                                                 //int AC IC_SetHWnd( HGRABBER hGrabber, __HWND hWnd ); ///< Sets a window handle for live display

    int IC_GetSerialNumber(int hgrabber, String serial);                                                    //int AC IC_GetSerialNumber( HGRABBER hGrabber, char* szSerial );///<Return the video capture device's serial number.

    int IC_ListDevices(String deviceList, int iSize);                                                       //int AC IC_ListDevices( char *szDeviceList, int iSize );///< Count and list devices.

    int IC_ListDevicesbyIndex(String deviceName, int size, int deviceIndex);                                //int AC IC_ListDevicesbyIndex( char *szDeviceName, int iSize, int DeviceIndex );

    int IC_ListVideoFormats(int hgrabber, String formatList, int size);                                     //int AC IC_ListVideoFormats( HGRABBER hGrabber, char *szFormatList, int iSize );///<List available video formats.

    int IC_ListVideoFormatbyIndex(int hgrabber, String formatName, int size, int index);                    //int AC IC_ListVideoFormatbyIndex( HGRABBER hGrabber, char *szFormatName, int iSize, int iIndex);

    int IC_GetDeviceCount();                                                                                //int AC IC_GetDeviceCount(); ///<Get the number of the currently available devices. 

    String IC_GetDevice(int i);                                                                             //char* AC IC_GetDevice( int iIndex ); ///< Get the name of a video capture device.

    String IC_GetUniqueNamefromList(int index);                                                             //char* AC IC_GetUniqueNamefromList( int iIndex );///< Get the unique name of a video capture device.

    int IC_GetInputChannelCount(int hgrabber);                                                              //int AC IC_GetInputChannelCount( HGRABBER hGrabber ); ///<Get the number of the available input channels.

    String IC_GetInputChannel(int hgrabber, int index);                                                     //char* AC IC_GetInputChannel( HGRABBER hGrabber, int iIndex ); ///<Get the name of an input channel.

    int IC_GetVideoNormCount(int hgrabber);                                                                 //int AC IC_GetVideoNormCount( HGRABBER hGrabber ); ///<Get the count of available video norms.

    String IC_GetVideoNorm(int hgrabber, int index);                                                        //char* AC IC_GetVideoNorm( HGRABBER hGrabber, int iIndex ); ///<Get the name of a video norm.

    int IC_GetVideoFormatCount(int hgrabber);                                                               //int AC IC_GetVideoFormatCount( HGRABBER hGrabber ); ///< Returns the count of available video formats.

    String IC_GetVideoFormat(int hgrabber, int index);                                                      //char* AC IC_GetVideoFormat( HGRABBER hGrabber, int iIndex ); ///<Return the name of a video format.

    int IC_SaveDeviceStateToFile(int hgrabber, String fileName);                                            //int AC IC_SaveDeviceStateToFile(HGRABBER hGrabber, char* szFileName);///<Save the state of a video capture device to a file. 

    int IC_LoadDeviceStateFromFile(int hgrabber, String fileName);                                          //HGRABBER AC IC_LoadDeviceStateFromFile(HGRABBER hGrabber, char* szFileName); ///<Load a device settings file.

    int IC_SaveDeviceSettings(int hgrabber, String fileName);                                               //int AC IC_SaveDeviceSettings( HGRABBER hGrabber, char* szFilename );

    int IC_OpenDeviceBySettings(int hgrabber, String fileName);                                             //int AC IC_OpenDeviceBySettings( HGRABBER hGrabber, char* szFilename );

    int IC_LoadDeviceSettings(int hgrabber, String fileName);                                               //int AC IC_LoadDeviceSettings( HGRABBER hGrabber, char* szFilename );

    int IC_OpenDevByDisplayName(int hgrabber, String dispName);                                             //int AC IC_OpenDevByDisplayName( HGRABBER hGrabber, char *szDisplayname); ///<Open a video capture by using its DisplayName. 

    int IC_GetDisplayName(int hgrabber, String dispName, int iLen);                                         //int AC IC_GetDisplayName( HGRABBER hGrabber, char *szDisplayname, int iLen); ///<Get the display name of a device.

    int IC_OpenDevByUniqueName(int hgrabber, String dispName);                                              //int AC IC_OpenDevByUniqueName( HGRABBER hGrabber, char *szDisplayname);

    int IC_GetUniqueName(int hgrabber, String uniqueName, int iLen);                                        //int AC IC_GetUniqueName( HGRABBER hGrabber, char *szUniquename, int iLen); ///<Get a UniqueName from a currently open device.

    int IC_IsDevValid(int hgrabber);                                                                        //int AC IC_IsDevValid( HGRABBER hGrabber); ///<Returns whether a video capture device is valid.

    int IC_ShowPropertyDialog(int hgrabber);                                                                //int AC IC_ShowPropertyDialog( HGRABBER hGrabber ); ///<Show the VCDProperty dialog. 

    int IC_ShowDeviceSelectionDialog(int hgrabber);                                                         //HGRABBER AC IC_ShowDeviceSelectionDialog( HGRABBER hGrabber ); ///<Show the device selection dialog.

    int IC_IsTriggerAvailable(int hgrabber);                                                                //int AC IC_IsTriggerAvailable( HGRABBER hGrabber ); ///<Check for external trigger support.

    int IC_EnableTrigger(int hgrabber, int enable);                                                         //int AC IC_EnableTrigger( HGRABBER hGrabber, int iEnable );

    void IC_RemoveOverlay(int hgrabber, int enable);                                                        //void AC IC_RemoveOverlay( HGRABBER hGrabber, int iEnable );

    void IC_EnableOverlay(int hgrabber, int enable);                                                        //void AC IC_EnableOverlay( HGRABBER hGrabber, int iEnable ); ///<Enable or disable the overlay bitmap.

    long IC_BeginPaint(int hgrabber);                                                                       //long AC IC_BeginPaint( HGRABBER hGrabber ); ///< BeginPaint returns an HDC for GDI painting purposes.

    void IC_EndPaint(int hgrabber);                                                                         //void AC IC_EndPaint( HGRABBER hGrabber ); ///< End painting functions on the overlay bitmap.

    void IC_MsgBox(String text, String title);                                                              //void AC IC_MsgBox( char * szText, char* szTitle ); ///<Display a windows messagebox.

    int IC_SetFrameReadyCallback(int hgrabber, int callback, Pointer d);                                    //int AC IC_SetFrameReadyCallback(HGRABBER hGrabber,FRAME_READY_CALLBACK	cb,void* x1_argument_in_void_userdata);

    int IC_SetCallbacks(int hgrabber, int callback, Pointer d1, int deviceLost, Pointer d2);                //int AC IC_SetCallbacks(HGRABBER	hGrabber,FRAME_READY_CALLBACK	cb,void* x1_argument_in_void_userdata,DEVICE_LOST_CALLBACK dlCB,void* x2_argument_in_void_userdata);

    int IC_SetContinuousMode(int hgrabber, int contMode);                                                   //int AC IC_SetContinuousMode( HGRABBER hGrabber, int cont ); ///<Set Continuous mode.

    int IC_SignalDetected(int hgrabber);                                                                    //int AC IC_SignalDetected( HGRABBER hGrabber  ); ///<Detects whether a video signal is available.

    int IC_GetTriggerModes(int hgrabber, String modeList, int size);                                        //int AC IC_GetTriggerModes( HGRABBER hGrabber,  char *szModeList, int iSize  ); ///<Get trigger modes.

    int IC_SetTriggerMode(int hgrabber, String szMode);                                                     //int AC IC_SetTriggerMode( HGRABBER hGrabber, char* szMode  ); ///<Set the trigger mode.

    int IC_SetTriggerPolarity(int hgrabber, int polarity);                                                  //int AC IC_SetTriggerPolarity( HGRABBER hGrabber, int iPolarity ); ///< Set the trigger polarity.

    int IC_GetExpRegValRange(int hgrabber, LongByReference lMin, LongByReference lMax);                     //int AC IC_GetExpRegValRange( HGRABBER hGrabber, long *lMin, long *lMax ); ///< Retrieve exposure register values lower and upper limits.

    int IC_GetExpRegVal(int hgrabber, LongByReference val);                                                 //int AC IC_GetExpRegVal( HGRABBER hGrabber, long *lValue ); ///< Retrieve the current register value of exposure.

    int IC_SetExpRegVal(int hgrabber, long val);                                                            //int AC IC_SetExpRegVal( HGRABBER hGrabber, long lValue ); ///<Set a register value for exposure.

    int IC_EnableExpRegValAuto(int hgrabber, int onOff);                                                    //int AC IC_EnableExpRegValAuto( HGRABBER hGrabber, int iOnOff ); ///<Enable or disable automatic of exposure.

    int IC_GetExpRegValAuto(int hgrabber, IntByReference onOff);                                            //int AC IC_GetExpRegValAuto( HGRABBER hGrabber, int *iOnOff );///<Check whether automatic exposure is enabled.

    int IC_IsExpAbsValAvailable(int hgrabber);                                                              //int AC IC_IsExpAbsValAvailable( HGRABBER hGrabber);

    int IC_GetExpAbsValRange(int hgrabber, FloatByReference fMin, FloatByReference fMax);                   //int AC IC_GetExpAbsValRange(HGRABBER hGrabber, float *fMin, float *fMax);

    int IC_GetExpAbsVal(int hgrabber, FloatByReference value);                                              //int AC IC_GetExpAbsVal(HGRABBER hGrabber, float *fValue);

    int IC_SetExpAbsVal(int hgrabber, float value);                                                         //int AC IC_SetExpAbsVal(HGRABBER hGrabber,  float fValue );

    int IC_GetColorEnhancement(int hgrabber, int onOff);                                                    //int AC IC_GetColorEnhancement(HGRABBER hGrabber, int *OnOff);

    int IC_SetColorEnhancement(int hgrabber, int onOff);                                                    //int AC IC_SetColorEnhancement(HGRABBER hGrabber, int OnOff);

    int IC_SoftwareTrigger(int hgrabber);                                                                   //int AC IC_SoftwareTrigger(HGRABBER hGrabber);

    int IC_SetFrameRate(int hgrabber, float frameRate);                                                     //int AC IC_SetFrameRate(HGRABBER hGrabber,float FrameRate);

    float IC_GetFrameRate(int hgrabber);                                                                    //float AC IC_GetFrameRate(HGRABBER hGrabber);

    int IC_SetWhiteBalanceAuto(int hgrabber, int onOff);                                                    //int AC IC_SetWhiteBalanceAuto( HGRABBER hGrabber, int iOnOff);

    int IC_SetWhiteBalanceRed(int hgrabber, long value);                                                    //int AC IC_SetWhiteBalanceRed(HGRABBER hGrabber, long Value);

    int IC_SetWhiteBalanceGreen(int hgrabber, int value);                                                   //int AC IC_SetWhiteBalanceGreen(HGRABBER hGrabber, long Value);

    int IC_SetWhiteBalanceBlue(int hgrabber, int value);                                                    //int AC IC_SetWhiteBalanceBlue(HGRABBER hGrabber, long Value);

    int IC_FocusOnePush(int hgrabber);                                                                      //int AC IC_FocusOnePush(HGRABBER hGrabber);

    int IC_ShowInternalPropertyPage(int hgrabber);                                                          //int AC IC_ShowInternalPropertyPage(HGRABBER hGrabber);

    int IC_ResetProperties(int hgrabber);                                                                   //int AC IC_ResetProperties(HGRABBER hGrabber);

    int IC_ResetUSBCam(int hgrabber);                                                                       //int AC IC_ResetUSBCam(HGRABBER hGrabber);

    int IC_QueryPropertySet(int hgrabber);                                                                  //int AC IC_QueryPropertySet(HGRABBER hGrabber);

    int IC_SetDefaultWindowPosition(int hgrabber, int defaultPos);                                          //int AC IC_SetDefaultWindowPosition(HGRABBER hGrabber, int Default);

    int IC_SetWindowPosition(int hgrabber, int px, int py, int w, int h);                                   //int AC IC_SetWindowPosition(HGRABBER hGrabber, int PosX, int PosY, int Width, int Height );

    int IC_enumProperties(int hgrabber, int cb, Pointer data);                                              //int AC IC_enumProperties(HGRABBER hGrabber, IC_ENUMCB cb, void* data);

    int IC_enumPropertyElements(int hgrabber, String property, int cb, Pointer data);                       //int AC IC_enumPropertyElements(HGRABBER hGrabber, char* Property, IC_ENUMCB cb, void* data);

    int IC_enumPropertyElementInterfaces(int hgrabber, String property, String element, int cb, Pointer data);  //int AC IC_enumPropertyElementInterfaces(HGRABBER hGrabber, char* Property, char* Element, IC_ENUMCB cb, void* data);

    int IC_IsPropertyAvailable(int hgrabber, String property, String element);                              //int AC IC_IsPropertyAvailable(HGRABBER hGrabber, char* Property, char *Element );

    int IC_GetPropertyValueRange(int hgrabber, String property, String element, IntByReference min, IntByReference max);//int AC IC_GetPropertyValueRange(HGRABBER hGrabber, char* Property, char *Element, int *Min, int *Max );

    int IC_GetPropertyValue(int hgrabber, String property, String element, IntByReference val);             //int AC IC_GetPropertyValue(HGRABBER hGrabber, char* Property, char *Element, int *Value );

    int IC_SetPropertyValue(int hgrabber, String property, String element, int value);                      //int AC IC_SetPropertyValue(HGRABBER hGrabber, char* Property, char *Element, int Value );

    int IC_GetPropertyAbsoluteValueRange(int hgrabber, String property, String element, FloatByReference min, FloatByReference max);//int AC IC_GetPropertyAbsoluteValueRange(HGRABBER hGrabber, char* Property, char *Element, float *Min, float *Max );

    int IC_GetPropertyAbsoluteValue(int hgrabber, String property, String element, FloatByReference value); //int AC IC_GetPropertyAbsoluteValue(HGRABBER hGrabber, char* Property, char *Element, float *Value );

    int IC_SetPropertyAbsoluteValue(int hgrabber, String property, String element, float value);            //int AC IC_SetPropertyAbsoluteValue(HGRABBER hGrabber, char* Property, char *Element, float Value );

    int IC_GetPropertySwitch(int hgrabber, String property, String element, IntByReference onOff);          //int AC IC_GetPropertySwitch(HGRABBER hGrabber, char* Property, char *Element, int *On );

    int IC_SetPropertySwitch(int hgrabber, String property, String element, int onOff);                     //int AC IC_SetPropertySwitch(HGRABBER hGrabber, char* Property, char *Element, int On );

    int IC_PropertyOnePush(int hgrabber, String property, String element);                                  //int AC IC_PropertyOnePush(HGRABBER hGrabber, char* Property, char *Element  );

    int IC_GetPropertyMapStrings(int hgrabber, String property, String element, IntByReference strCount, String str);//int AC IC_GetPropertyMapStrings(HGRABBER hGrabber, char* Property, char *Element, int *StringCount, char **Strings  );

    int IC_GetPropertyMapString(int hgrabber, String property, String element, String str);                 //int AC  IC_GetPropertyMapString(HGRABBER hGrabber, char* Property, char *Element,  char *String );

    int IC_SetPropertyMapString(int hgrabber, String property, String element, String str);                 //int AC  IC_SetPropertyMapString(HGRABBER hGrabber, char* Property, char *Element,  char *String );

    int IC_GetAvailableFrameFilterCount();                                                                  //int AC IC_GetAvailableFrameFilterCount();

    int IC_GetAvailableFrameFilters(String filterList, int size);                                           //int AC IC_GetAvailableFrameFilters(char **szFilterList, int iSize );

    int IC_CreateFrameFilter(String filterName, IntByReference fHandle);                                    //int AC IC_CreateFrameFilter(char *szFilterName, HFRAMEFILTER *FilterHandle );

    int IC_AddFrameFilterToDevice(int hgrabber, IntByReference fHandle);                                    //int AC IC_AddFrameFilterToDevice(HGRABBER hGrabber, HFRAMEFILTER FilterHandle );

    void IC_DeleteFrameFilter(IntByReference fHandle);                                                      //void AC IC_DeleteFrameFilter( HFRAMEFILTER FilterHandle );

    int IC_FrameFilterShowDialog(IntByReference fHandle);                                                   //int AC IC_FrameFilterShowDialog( HFRAMEFILTER FilterHandle );

    int IC_FrameFilterGetParameter(IntByReference fHandle, String pName, Pointer data);                     //int AC IC_FrameFilterGetParameter(HFRAMEFILTER FilterHandle, char* ParameterName, void* Data );

    int IC_FrameFilterSetParameterFloat(IntByReference fHandle, String pName, float data);                  //int AC IC_FrameFilterSetParameterFloat(HFRAMEFILTER FilterHandle, char* ParameterName, float Data );

    int IC_FrameFilterSetParameterBoolean(IntByReference fHandle, String pName, int data);                  //int AC IC_FrameFilterSetParameterBoolean(HFRAMEFILTER FilterHandle, char* ParameterName, int Data );

    int IC_FrameFilterSetParameterString(IntByReference fHandle, String pName, String data);                //int AC IC_FrameFilterSetParameterString(HFRAMEFILTER FilterHandle, char* ParameterName, char* Data );

    int IC_FrameFilterDeviceClear(int hgrabber);                                                            //int AC IC_FrameFilterDeviceClear(HGRABBER hGrabber );

    void IC_enumCodecs(int cb, Pointer data);                                                               //void AC IC_enumCodecs(ENUMCODECCB cb, void* data);

    int IC_Codec_Create(String name);                                                                       //HCODEC IC_Codec_Create(char* Name);

    void IC_Codec_Release(int codec);                                                                       //void AC IC_Codec_Release(HCODEC Codec);

    int IC_Codec_getName(int codec, int i, String name);                                                    //int AC IC_Codec_getName(HCODEC Codec, int l, char* Name);

    int IC_Codec_hasDialog(int codec);                                                                      //int AC IC_Codec_hasDialog(HCODEC Codec);

    int IC_Codec_showDialog(int codec);                                                                     //int AC IC_Codec_showDialog(HCODEC Codec);

    int IC_SetCodec(int hgrabber, int codec);                                                               //int AC IC_SetCodec(HGRABBER hlGrabber,HCODEC Codec);

    int IC_SetAVIFileName(int hgrabber, String fileName);                                                   //int IC_SetAVIFileName(HGRABBER hlGrabber,char * FileName);

    int IC_enableAVICapturePause(int hgrabber, int pause);                                                  //int IC_enableAVICapturePause(HGRABBER hlGrabber, int Pause ); 
}
