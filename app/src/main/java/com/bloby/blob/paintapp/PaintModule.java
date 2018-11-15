package com.bloby.blob.paintapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;



import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import uz.shift.colorpicker.LineColorPicker;


public class PaintModule extends Activity
{
    FrameLayout shapesLayout;

    LinearLayout layout_seekbar;
    LinearLayout colorpicker_bar;
    //   LinearLayout addshape_popup,drawshape_popup;
    int viewheight;



    static RelativeLayout layBg;

    static LinearLayout.LayoutParams paint_penactive,paint_penpassive;

    Button circle,square,triangle,pentagon,hexagon,vertical_line,horizontal_line,draw_circle,
            draw_square,draw_triangle,draw_line;
    static Button marker;
    static Button pen;
    static Button  shapes_btn,erase,undo,redo,back,options;
    static Button select_path;

    static ImageView crop_img;

    static Boolean notPasted=false;
    Boolean iserase=false,isundo=false,isredo=false;

    int _xDelta, _yDelta;
    int getvalue=0;
    int count = 1000;
    static  int strokeWidthPaint=0;
    int displayWidth,dispalayHeight;
    int id_shapeactivity=1;
    int imgids=0;
    int count_ofShapes=0;

    float startDegree=44;

    Bitmap bmp;
    static Bitmap cropped_bitmap;

    static Context context;

    static ArrayList<String> path_aftercropPaste=new ArrayList<String>();
    static ArrayList<String> path_beforecropPaste=new ArrayList<String>();


    static PaintModuleCanvas draw_clone;



    String path_savecanvas;
    String pathFile;
    String path_noview=new String(),path_withview=new String();
    static String cropped_mainCanvasPath;

    static SeekBar seek_brush_size;
    static LineColorPicker colorPicker;


    int paintId=1;
    String fullpath;
    String noviewpath;
    String docTitle;
    Long documentid;
    static Boolean hadCropped = false;
    Button btnBack;
    static int seekCount=0;
    boolean cropTest=false;

    /*Fro Smart Banner Ads*/

    LinearLayout ll_AdView;
   // AdView mAdView;

    /*Fro Smart Banner Ads*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("PaintModule called");
        viewheight=(int)(0.3*screendimension());

        context=PaintModule.this;
        //getSupportActionBar().hide();
        setContentView(R.layout.paint_activity);



        // HandleAppCrash.deploy(this,CrashReportActivity.class);

         /*For Smart Banner Ads*/

        ll_AdView = (LinearLayout)findViewById(R.id.ll_ad_view_Paint);
      /*  mAdView = (AdView)findViewById(R.id.adView_Paint);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/
        /*For Smart Banner Ads*/

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor2 = pref1.edit();
        editor2.putInt("dbcount", 1);
        Log.v("duplicateinc", "hereinfilenavigation");
        editor2.commit();
        findViews();
        draw_clone.changePaint();
        setUndoPassive();
        setRedoPassive();
        Bundle extra=getIntent().getExtras();
        if(extra!=null){
            getvalue=extra.getInt("id");
            fullpath=extra.getString("fullpath");
            noviewpath=extra.getString("noviewpath");
            documentid=  extra.getLong("document_id");
            docTitle=extra.getString("title_for_document");
            Log.v("getVal", String.valueOf(getvalue));
            Log.v("fullpath", String.valueOf(fullpath));
            Log.v("noviewpath", String.valueOf(noviewpath));

            if(noviewpath!= null)
            {
                draw_clone.changePaint();
                draw_clone.getpaths(noviewpath);
                System.out.println("only paint no path");
            }
            else  if (noviewpath == null) {
                draw_clone.getpathnull();
                System.out.println("path is null");
            }
        }

       /* if(shapeRepo.getCounts()!=0){
            int newCount=shapeRepo.getCounts();
            count=newCount+1;
            Log.v("countVAl", String.valueOf(count));
        }
*/
/*

        Cursor curPaintid=paintRepo.getAudioId();
        Log.v("curpaintidSize", String.valueOf(curPaintid.getCount()));

        if(curPaintid!=null && curPaintid.getCount()!=0){
            curPaintid.moveToFirst();

            do{
                Log.v("vechecking","are here");
                int dupId=curPaintid.getInt(0);
                paintId=dupId+1;
                Log.v("vechecking", String.valueOf(paintId));

            }while(curPaintid.moveToNext());
        }

        if(getvalue!=0){
            paintId=getvalue;
        }

*/



        // intentFromTextnotepage();

      /*  int count_incrementShapeId=shapeRepo.getIncrementShapeId();
        if(count_incrementShapeId!=0){
            count=count_incrementShapeId+1;
        }
        Cursor cur=shapeRepo.getIdActivity();
        if(cur!=null){
            if(cur.moveToFirst()){
                do{
                    int idss=cur.getInt(1);
                    id_shapeactivity=idss+1;
                }while(cur.moveToNext());
            }
        }
        Cursor cur1=shapeRepo.getline();
        if(cur1!=null){
            if(cur1.moveToFirst()){
                do{
                }while(cur1.moveToNext());
            }
        }
*/


        pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(draw_clone.ctrl_crop && cropTest) {
                    draw_clone.clearCropPointcheck();
                    draw_clone.ctrl_crop=false;
                }
                draw_clone.clearCropPoint();
                try {
                    if (!hadCropped) {
                        draw_clone.disablePaintOption();
                        disablePen();
                        pen.setBackgroundResource(R.drawable.paint_pen_active);
                        draw_clone.isStart_paint = true;
                        choosePen();
                    } else {
                        showAlertCroppedImageActive();
                    }
                }

                catch (Exception e)
                {

                }

/*
                if (!draw_clone.stop_paint) {
                    defaultStateOfTools();
                    disableShape();
                    pen.setLayoutParams(paint_penactive);
                    colorPickerOnclick();
                    seekbarOnclick();
                    draw_clone.drawPen();
                    penMarkerCondition();
                }
*/
            }
        });
        marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(draw_clone.ctrl_crop && cropTest) {
                    Log.v("ctrl_crop","inside_we are there");
                    draw_clone.clearCropPointcheck();
                    draw_clone.ctrl_crop=false;
                }
                draw_clone.clearCropPoint();
                try{
                    if(!hadCropped) {
                        draw_clone.disablePaintOption();
                        disablePen();
                        marker.setBackgroundResource(R.drawable.paint_marker_active);
                        draw_clone.isStart_paint = true;
                        chooseMarker();
                    }
                    else
                    {
                        showAlertCroppedImageActive();
                    }
                }
                catch (Exception e)
                {

                }

/*
                if (!draw_clone.stop_paint) {
                    defaultStateOfTools();
                    disableShape();
                    marker.setLayoutParams(paint_penactive);
                    colorPickerOnclick();
                    seekbarOnclick();
                    draw_clone.drawMarker();
                    penMarkerCondition();
                }
*/
            }
        });

        erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(draw_clone.ctrl_crop && cropTest) {
                    draw_clone.clearCropPointcheck();
                    draw_clone.ctrl_crop=false;
                }
                draw_clone.clearCropPoint();
                try{
                    if(!hadCropped){
                        draw_clone.disablePaintOption();
                        disablePen();
                        erase.setBackgroundResource(R.drawable.paint_eraser_active);
                        draw_clone.isStart_paint = true;
                        chooseEraser();
                    }
                    else
                    {
                        showAlertCroppedImageActive();
                    }
                }
                catch (Exception e)
                {

                }

/*
                if (!draw_clone.stop_paint) {
                    defaultStateOfTools();
                    disableShape();
                    colorpicker_bar.setVisibility(View.GONE);
                    erase.setLayoutParams(paint_penactive);
                    seekbarOnclick();
                    iserase = true;
                    draw_clone.setErase(true);
                }
*/
            }
        });
        select_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disablePen();
                draw_clone.ctrl_crop=true;
                draw_clone.changeCrop();
                defaultStateOfTools();
                select_path.setBackgroundResource(R.drawable.paint_crop_active);
                draw_clone.isStart_paint = true;
                cropTest=true;
/*
                try{
                    if(!hadCropped) {
                        if (!draw_clone.iscrop) {
                            draw_clone.disablePaintOption();
                            defaultStateOfTools();
                            disableShape();
                            // draw_clone.initCrop();
                            select_path.setBackgroundResource(R.drawable.paint_crop_active);
                            disables();
                            draw_clone.changeCrop();
                        }
                    }
                    else
                    {
                        showAlertCroppedImageActive();
                    }
                }
                catch (Exception e)
                {

                }
*/
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!hadCropped) {
                        if (!draw_clone.stop_paint) {
                            //  defaultStateOfTools();

                            draw_clone.undoPaths();
                        }
                    }
                    else
                    {
                        showAlertCroppedImageActive();
                    }
                }
                catch (Exception e)
                {

                }
            }
        });
        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!hadCropped) {
                        if (!draw_clone.stop_paint) {
                            //  defaultStateOfTools();

                            draw_clone.redoPaths();
                        }
                    }
                    else
                    {
                        showAlertCroppedImageActive();
                    }
                }
                catch (Exception e)
                {

                }
            }
        });
        shapes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                disablePen();
                if(draw_clone.ctrl_crop && cropTest) {
                    Log.v("Inside_shapes","button_click");
                    draw_clone.clearCropPointcheck();
                    draw_clone.ctrl_crop=false;
                }
                draw_clone.clearCropPoint();
                draw_clone.changePaint();
                draw_clone.isStart_paint = false;
                //draw_clone.clearCropPoint();
                try{
                    if(!hadCropped) {
                        if (!draw_clone.stop_paint) {
                            draw_clone.disablePaintOption();
                            defaultStateOfTools();
                            shapesLayout.setVisibility(View.VISIBLE);
                            shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
                            draw_clone.initiateDrawShapes();
                        }
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!draw_clone.iscrop) {
                                    draw_clone.setpaintcolors(colorPicker.getColor());
                                    if (!draw_clone.stop_paint) {
                                        draw_clone.isStart_paint = true;
                                    }
                                    if (draw_clone.stop_paint) {
                                        draw_clone.isStart_paint = false;
                                    }
                                }
                            }
                        });
                        //disableShape();
                        //  draw_clone.setErase(false);
                        // draw_clone.setNormal();
/*                    PopupMenu menuOptions = new PopupMenu(PaintModule.this, shapes_btn);
                    menuOptions.inflate(R.menu.shapes_option);
                    menuOptions.show();
                    menuOptions.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.draw_shapes:
                                    menuOptions.dismiss();
                                   // drawshape_popup.setVisibility(View.VISIBLE);
                                    shapes_btn.setBackgroundResource(R.drawable._p_shapes_ho);
                                    draw_clone.initiateDrawShapes();
                                    colorPicker.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            draw_clone.setpaintcolors(colorPicker.getColor());
                                            if (!draw_clone.stop_paint) {
                                                draw_clone.isStart_paint = true;
                                            }
                                            if (draw_clone.stop_paint) {
                                                draw_clone.isStart_paint = false;
                                            }
                                        }
                                    });
                                    break;
                                case R.id.add_shapes:
                                    menuOptions.dismiss();
                                  //  addshape_popup.setVisibility(View.VISIBLE);
                                    shapes_btn.setBackgroundResource(R.drawable._p_shapes_ho);
                                    break;
                            }
                            return false;
                        }
                    });*/
                    }
                    else
                    {
                        showAlertCroppedImageActive();
                    }

                }
                catch (Exception e)
                {

                }
            }
        });

      /*  circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipCanvasPaint ca = new ClipCanvasPaint(PaintModule.this);
                layBg.addView(ca);
                Log.v("countBefore", String.valueOf(count));
                ca.setId(count++);
                Log.v("countAfter", String.valueOf(count));
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                *//*shapeModels.setRight(-9999999);*//*
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(7);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                Log.v("paintIdAct", String.valueOf(paintId));
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");//doc id
                shapeRepo.insertline(shapeModels);

                ca.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();
                        seekbarStrokeAddShape(ca);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                // ca.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    ca.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    ca.fillColors(colorPicker.getColor());
                                }
                            }
                        });
                    }
                });
                ca.setShapeType(7);
                ca.getcountidvalue(-1);
                // addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });
        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipCanvasPaint rect = new ClipCanvasPaint(PaintModule.this);
                layBg.addView(rect);
                rect.setId(count++);
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(3);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");
                shapeRepo.insertline(shapeModels);

                rect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();
                        seekbarStrokeAddShape(rect);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                //rect.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    rect.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    rect.fillColors(colorPicker.getColor());
                                }
                            }
                        });

                    }
                });
                rect.setShapeType(3);
                // addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });

        triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipCanvasPaint triangle = new ClipCanvasPaint(PaintModule.this);
                layBg.addView(triangle);
                triangle.setId(count++);
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(4);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");
                shapeRepo.insertline(shapeModels);

                triangle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();

                        seekbarStrokeAddShape(triangle);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                //triangle.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    triangle.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    triangle.fillColors(colorPicker.getColor());
                                }
                            }
                        });
                    }
                });
                triangle.setShapeType(4);

                //addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });
        pentagon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipCanvasPaint pentagon = new ClipCanvasPaint(PaintModule.this);

                layBg.addView(pentagon);
                pentagon.setId(count++);
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(6);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");
                shapeRepo.insertline(shapeModels);
                pentagon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();

                        seekbarStrokeAddShape(pentagon);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                //pentagon.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    pentagon.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    pentagon.fillColors(colorPicker.getColor());
                                }
                            }
                        });
                    }
                });
                pentagon.setShapeType(6);

                // addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });


        hexagon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipCanvasPaint hexagon = new ClipCanvasPaint(PaintModule.this);
                layBg.addView(hexagon);
                hexagon.setId(count++);
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(5);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");
                shapeRepo.insertline(shapeModels);
                hexagon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();
                        seekbarStrokeAddShape(hexagon);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                //hexagon.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    hexagon.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    hexagon.fillColors(colorPicker.getColor());
                                }
                            }
                        });
                    }
                });
                hexagon.setShapeType(5);
                //addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });
        vertical_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipCanvasPaint vline = new ClipCanvasPaint(PaintModule.this);

                layBg.addView(vline);
                vline.setId(count++);
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(1);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");
                shapeRepo.insertline(shapeModels);
                vline.idsvalue(count);
                vline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();
                        seekbarStrokeAddShape(vline);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                //vline.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    vline.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    vline.fillColors(colorPicker.getColor());
                                }
                            }
                        });
                    }
                });
                vline.setShapeType(1);
                //draw_clone.getcountdraw(0);
               *//* vline.getcountidvalue(id_shapeactivity);
                draw_clone.getcountdraw(0);
                shapeModels.setViewids(id_shapeactivity);
                shapeModels.setIdshapes(count-1);
                shapeRepo.insetviews(shapeModels);*//*
                // addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });

        horizontal_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ClipCanvasPaint hline = new ClipCanvasPaint(PaintModule.this);

                layBg.addView(hline);
                hline.setId(count++);
                shapeModels.setAttachId(count-1);
                shapeModels.setTop(0);
                shapeModels.setLeft(0);
                shapeModels.setHeight(viewheight);
                shapeModels.setWidth(viewheight);
                shapeModels.setShapeVal(2);
                shapeModels.setColorcount(Color.BLACK);
                shapeModels.setColorfill(Color.TRANSPARENT);
                shapeModels.setStroke(5);
                shapeModels.setRotate(0);
                shapeModels.setPaintid(paintId);
                shapeModels.setDocid("xx");
                shapeRepo.insertline(shapeModels);

                hline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        disableall();
                        seekbarStrokeAddShape(hline);
                        colorPicker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                draw_clone.setpaintcolors(colorPicker.getColor());
                                //hline.strokeColor(colorPicker.getColor());

                                SharedPreferences sha=getSharedPreferences("fill",0);
                                String sha1=sha.getString("fill",null);

                                if(sha1.equals("0")){
                                    hline.strokeColor(colorPicker.getColor());
                                }
                                else{
                                    hline.fillColors(colorPicker.getColor());
                                }
                            }
                        });
                    }
                });
                hline.setShapeType(2);

                //addshape_popup.setVisibility(View.GONE);
                shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
            }
        });

*/

        draw_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    defaultStateOfTools();

                    shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
                    chooseDrawLine();
                }
                catch (Exception e)
                {

                }

            }
        });

        draw_clone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.v("hellow","worldOccur");
                disableall();
                SharedPreferences sha=getSharedPreferences("fill",0);
                SharedPreferences.Editor shaEdit=sha.edit();
                shaEdit.putString("fill","0");
                shaEdit.commit();

                if(seekCount==1){
                    seekbarOnclick();
                    seekCount=0;
                }
                return false;
            }

        });

        draw_square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    defaultStateOfTools();
                    shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
                    chooseDrawRectangle();
                }
                catch (Exception e)
                {

                }


            }
        });

        draw_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    defaultStateOfTools();
                    shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
                    chooseDrawCircle();
                }
                catch (Exception e)
                {

                }

            }
        });

        draw_triangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    defaultStateOfTools();
                    shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
                    chooseDrawTriangle();
                }
                catch (Exception e)
                {

                }

            }
        });
        erase.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try{
                    defaultStateOfTools();

                    erasePopup(view);
                }
                catch (Exception e)
                {

                }
                return false;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    draw_clone.changePaint();
                    backFunctionality();
                }
                catch (Exception e)
                {

                }
            }
        });

      /*  shapeModels.setShapesetid(id_shapeactivity);
        shapeRepo.insertid(shapeModels);
        Log.v("getvaluecount_", String.valueOf(getvalue));
        Cursor g1=shapeRepo.getshape(getvalue);

        Log.v("cursor_count", String.valueOf(g1.getCount()));
        if(g1!=null){
            if(g1.moveToFirst())
            {
                do{
                    imgids=g1.getInt(12);
                    shapeRepo.updatepaintid(id_shapeactivity,g1.getInt(1));
                    shapeRepo.updateview(id_shapeactivity,g1.getInt(1));
                    Cursor viewval=shapeRepo.getviewvalue();
                    if(viewval!=null){
                        if(viewval.moveToFirst()){
                            do{}while (viewval.moveToNext());
                        }
                    }
                    Cursor cursors=shapeRepo.getline();
                    if(cursors!=null){
                        if(cursors.moveToFirst()){
                            do{}while(cursors.moveToNext());
                        }
                    }
                    final ClipCanvasPaint ca =new ClipCanvasPaint(PaintModule.this);
                    ca.setId(g1.getInt(1));
                    int k1=250-g1.getInt(7);
                    int k2=250-g1.getInt(6);
                    ca.layoutParams=new RelativeLayout.LayoutParams(g1.getInt(5),g1.getInt(4));
                    ca.layoutParams.setMargins(g1.getInt(3),g1.getInt(2),-9999999,-9999999);
                    ca.layGroup.setLayoutParams(ca.layoutParams);
                    layBg.addView(ca.layGroup);
                    ca.getRegenerateShape(g1.getInt(6),g1.getInt(5),g1.getInt(4));
                    ca.setShapeType(g1.getInt(6));
                    ca.setcolors(g1.getInt(7),g1.getInt(8),g1.getInt(9));
                    ca.layGroup.setRotation(g1.getInt(10));
                    disableall();
                    ca.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            disableall();
                        }
                    });

                }while(g1.moveToNext());
            }
        }*/

        intialiseDefaultColorToPen();
    }

/*
    public static void seekbarStrokeAddShape(ClipCanvasPaint ca)
    {

        seekCount=1;
        seek_brush_size.setProgress(2);
        seek_brush_size.setMax(20);
*/
/*draw_clone.setPaintStrokeWidth(40);*//*

        seek_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int count = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                count = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ca.strokeAddShape((count*2)-count);
*/
/*strokeWidthPaint = count * 2;*//*

            }
        });
    }
*/




    public static void disablePen()
    {
        try{
            pen.setBackgroundResource(R.drawable.paint_pen_passive);
            marker.setBackgroundResource(R.drawable.paint_marker_passive);
            erase.setBackgroundResource(R.drawable.paint_eraser_passive);
            shapes_btn.setBackgroundResource(R.drawable.paint_shape_passive);
        }
        catch (Exception e)
        {

        }
    }
    public void findViews()
    {
        btnBack = (Button)findViewById(R.id.back_btn);
        shapesLayout = (FrameLayout)findViewById(R.id.shapes_layout);

        draw_clone= (PaintModuleCanvas) findViewById(R.id.draw_view1);


        colorpicker_bar=(LinearLayout)findViewById(R.id.colorpicker_bar);
        paint_penactive=new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.paint_pen_eraser_active),
                (int)getResources().getDimension(R.dimen.paint_pen_eraser_active));
        paint_penactive.gravity=Gravity.BOTTOM;
        paint_penpassive=new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.paint_pen_eraser)
                ,(int)getResources().getDimension(R.dimen.paint_pen_eraser));
        paint_penpassive.gravity=Gravity.BOTTOM;
        crop_img=(ImageView)findViewById(R.id.crop_img);
        layout_seekbar=(LinearLayout)findViewById(R.id.seekbar);
        pen= (Button) findViewById(R.id.pen);
        erase = (Button)findViewById(R.id.paint_eraser);
        select_path= (Button)findViewById(R.id.select_path);
        undo = (Button) findViewById(R.id.undo);
        redo = (Button)findViewById(R.id.redo);
        marker=(Button)findViewById(R.id.marker);
        shapes_btn=(Button)findViewById(R.id.shapes);
        /*circle=(Button)findViewById(R.id.circle);
        square=(Button)findViewById(R.id.rectangle);
        triangle=(Button)findViewById(R.id.triangle);
        pentagon=(Button)findViewById(R.id.pentagon);
        hexagon=(Button)findViewById(R.id.hexagon);
        vertical_line=(Button)findViewById(R.id.hori_line);
        horizontal_line=(Button)findViewById(R.id.verti_line);*/
        seek_brush_size= (SeekBar) findViewById(R.id.seek_brush_size);
        draw_circle=(Button)findViewById(R.id.draw_circle);
        draw_square=(Button)findViewById(R.id.draw_rectangle);
        draw_triangle=(Button)findViewById(R.id.draw_triangle);
        draw_line=(Button)findViewById(R.id.draw_verti_line);
        //  addshape_popup= (LinearLayout) findViewById(R.id.addshape_popup);
        //  drawshape_popup=(LinearLayout) findViewById(R.id.draw_shapes_popup);
        colorPicker = (LineColorPicker)findViewById(R.id.picker);
        layBg = (RelativeLayout) findViewById(R.id.laybg);
    }

    ///////////////////////////////////////////////////////////////////
    ///////////////////free hand sketch///////////////////////////////
    //////////////////////////////////////////////////////////////////

    public void choosePen()
    {
        draw_clone.changePaint();
        draw_clone.initiatePen();
        defaultStateOfTools();
        pen.setLayoutParams(paint_penactive);
        colorPickerOnclick();
        seekbarOnclick();
        penMarkerCondition();
    }

    public void chooseMarker()
    {
        try{
            draw_clone.changePaint();
            draw_clone.initiateMarker();
            defaultStateOfTools();
            marker.setLayoutParams(paint_penactive);
            colorPickerOnclick();
            seekbarOnclick();
            penMarkerCondition();
        }
        catch (Exception e)
        {

        }
    }
    public void chooseEraser()
    {
        try{
            draw_clone.changePaint();
            draw_clone.initiateEraser();
            defaultStateOfTools();
            seekbarOnclick();
            // colorpicker_bar.setVisibility(View.GONE);
            erase.setLayoutParams(paint_penactive);
        }
        catch (Exception e)
        {

        }
    }
    //////////////////////////////////////////////////////////////////
    ////////////////////free hand sketch ends/////////////////////////
    /////////////////////////////////////////////////////////////////


    //////////////////////////////////////////////////////////////////
    ///////////////////draw shapes///////////////////////////////////
    ////////////////////////////////////////////////////////////////

    public void drawShapesSelectedState()
    {
        //   drawshape_popup.setVisibility(View.GONE);
        shapes_btn.setBackgroundResource(R.drawable.paint_shape_active);
    }

    public void chooseDrawLine()
    {
        draw_clone.isStart_paint = true;
        draw_clone.initiateDrawLine();
        drawShapesSelectedState();
    }
    public void chooseDrawRectangle()
    {
        draw_clone.isStart_paint = true;
        draw_clone.initiateDrawRectangle();
        drawShapesSelectedState();
    }
    public void chooseDrawCircle()
    {
        draw_clone.isStart_paint = true;
        draw_clone.initiateDrawCircle();
        drawShapesSelectedState();
    }
    public void chooseDrawTriangle()
    {
        draw_clone.isStart_paint = true;
        draw_clone.initiateDrawTriangle();
        drawShapesSelectedState();
    }

    ////////////////////////////////////////////////////////////////
    /////////////////////draw shapes ends//////////////////////////
    //////////////////////////////////////////////////////////////



    public void disableall() {

       /* for (int i = 0; i < layBg.getChildCount(); i++) {
            if (layBg.getChildAt(i) instanceof ClipCanvasPaint) {
                ((ClipCanvasPaint) layBg.getChildAt(i)).disableAll();
            }
        }
        for (int i = 0; i < layBg.getChildCount(); i++) {
            if (layBg.getChildAt(i) instanceof ClipArtcrop) {
                ((ClipArtcrop) layBg.getChildAt(i)).disableAll();
            }
        }*/
    }
    public void getCountOfAddedShapes()
    {
        Log.v("getvaluecount", String.valueOf(getvalue));
      /*  Cursor g1=shapeRepo.getshape(getvalue);
        count_ofShapes = layBg.getChildCount();
        Log.v("cursor_count1", String.valueOf(g1.getCount()));
        Log.v("laybgchild", String.valueOf(layBg.getChildCount()));*/
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            try{
                draw_clone.changePaint();
                backFunctionality();
            }
            catch (Exception e)
            {

            }
           /* if(draw_clone.stop_paint)
            {
                Toast.makeText(getApplicationContext(),"Delete or paste the cropped portion",Toast.LENGTH_LONG).show();
            }
            if (!draw_clone.stop_paint) {
                draw_clone.checkPageHasPaint();
                getCountOfAddedShapes();
                disableall();
                if (draw_clone.isPainted || count_ofShapes > 1) {
                    System.out.println("count_ofShapes "+count_ofShapes);
                    saveAndIntent();
                    //  saveCanvaspicWithoutview();
                    // save_CanvasPic();
                   *//* Cursor cur6 = shapeRepo.getline();
                    if (cur6 != null) {
                        if (cur6.moveToFirst()) {
                            do {

                            } while (cur6.moveToNext());
                        }
                    }*//*
                }
                else
                {
                    Log.v("Yes","we are entered");
                    //paintRepo.delete_view(getvalue);
                    Intent intent = new Intent(PaintModule.this, TextNotePage.class);
                    setResult(11,intent);
                    finish();
                }
return false;
            }*/

        }
        return super.onKeyDown(keyCode, event);
    }
    public void backFunctionality()
    {

        saveAndIntent();
/*
        try{
            if(draw_clone.stop_paint)
            {
                Toast.makeText(getApplicationContext(),"Delete or paste the cropped portion",Toast.LENGTH_LONG).show();
            }
            if (!draw_clone.stop_paint) {
                draw_clone.checkPageHasPaint();
                getCountOfAddedShapes();
                disableall();
                if (draw_clone.isPainted || count_ofShapes > 1) {
                    System.out.println("count_ofShapes " + count_ofShapes);
                    saveAndIntent();
                    //  saveCanvaspicWithoutview();
                    // save_CanvasPic();
                   */
/* Cursor cur6 = shapeRepo.getline();
                    if (cur6 != null) {
                        if (cur6.moveToFirst()) {
                            do {

                            } while (cur6.moveToNext());
                        }
                    }*//*

                } else {
                    Log.v("Yes", "we are entered");
                    //paintRepo.delete_view(getvalue);
                    Intent intent = new Intent(PaintModule.this, TextNotePage.class);
                    setResult(11, intent);
                    finish();

                }

            }
        }
        catch (Exception e)
        {

        }
*/
    }
    public Bitmap getBitmap()
    {
        layBg.setDrawingCacheEnabled(true);
        layBg.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(layBg.getDrawingCache());
        layBg.setDrawingCacheEnabled(false);
        return bmp;
    }
    public static void showAlertCroppedImageActive() {
        System.out.println("showAlertCroppedImageActive called");
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Alert");
        alert.setMessage("Paste or close the cropped paint");
        alert.setPositiveButton("OK",null);
        alert.show();
    }



    public static void initiatePaint()
    {
        try{
            draw_clone.stop_paint=false;
            draw_clone.isStart_paint=true;
            select_path.setBackgroundResource(R.drawable.paint_crop_passive);


            draw_clone.disableCrop();
            draw_clone.disablePaintOption();
            disablePen();
            pen.setBackgroundResource(R.drawable.paint_pen_active);
            draw_clone.initiatePen();
            pen.setLayoutParams(paint_penactive);
            colorPickerOnclick();
            seekbarOnclick();
            penMarkerCondition();
            draw_clone.invalidate();
        }
        catch (Exception e)
        {

        }
    }
    public static void deleteImage(String path)
    {
        try{
            File fdelete = new File(path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {}
                else {}
            }
        }
        catch (Exception e)
        {

        }
    }

/*
    public void saveCanvaspicWithoutview()
    {
        disables();
        View content = layBg;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"EvernoteClone/Pictures");
        if(!file.exists()){
            file.mkdirs();
        }
        String paint_path_noView=file+"/"+"paint_"+System.currentTimeMillis()+".png";
        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(paint_path_noView);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error",Toast.LENGTH_SHORT).show();
        }
        if(getvalue==0) {
            Intent i = new Intent(PaintModule.this, TextNotePage.class);
            i.putExtra("shapesDraw", "shapesDraw");
            i.putExtra("byteArray", path_savecanvas);
            i.putExtra("withoutshape",paint_path_noView);
            setResult(3, i);
            finish();
        }
        else{
            paintRepo.updateuri(getvalue,path_savecanvas);
            paintRepo.updateNoShapes(getvalue,paint_path_noView);
            Intent i = new Intent(PaintModule.this, TextNotePage.class);
            i.putExtra("val","get");
            i.putExtra("val1",imgids);
            setResult(6,i);
            finish();
        }
        deleteCroppedImages();
    }
*/



/*
    public void save_CanvasPic()
    {
        View content = layBg;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"EvernoteClone/Pictures");
        if(!file.exists()){
            file.mkdirs();
        }
        String pathFile=file+"/"+"paint_"+System.currentTimeMillis()+".png";
        path_savecanvas=pathFile;
        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(pathFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error",Toast.LENGTH_SHORT).show();
        }
    }
*/

    public void disableShape()
    {
        draw_clone.disableShape();
    }
    public void erasePopup(View view)
    {
        List<String> popup_list = new ArrayList<String>();

        popup_list.add("Clear Paint");
        // popup_list.add("Clear Page");

        final PopupMenu menu = new PopupMenu(PaintModule.this, view);
        for (String s : popup_list) {
            menu.getMenu().add(s);
        }
        menu.show();

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {


                if (menuItem.getTitle() == "Clear Paint") {

                    draw_clone.clearPaint();

                }

           /* if (menuItem.getTitle() == "Clear Page") {

                lineSelected();

            }*/
                return false;
            }
        });
    }


   /* public void lineSelected()
    {
        draw_clone.lineSelected();

    }

    public void rectSelected()
    {
        draw_clone.rectSelected();
    }

    public void circleSelected()
    {
        draw_clone.circleSelected();

    }

    public void triangleSelected()
    {
        draw_clone.triangleSelected();
    }
*/

    public void disables(){

/*
        for (int i = 0; i < layBg.getChildCount(); i++) {

            if (layBg.getChildAt(i) instanceof ClipCanvasPaint) {

                ((ClipCanvasPaint) layBg.getChildAt(i)).removeAllViews();
            }

        }
*/

    }

    public void intialiseDefaultColorToPen()
    {
        defaultStateOfTools();
        pen.setLayoutParams(paint_penactive);
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw_clone.setpaintcolors(colorPicker.getColor());
            }
        });
        seekbarOnclick();
        draw_clone.drawPen();
        draw_clone.isStart_paint = true;
        draw_clone.setpaintcolors(Color.parseColor("#9c27b0"));
        penMarkerCondition();

    }
    public boolean onTouchEvent(View view,MotionEvent event) {

        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        layBg.invalidate();
        return true;
    }

    public void defaultStateOfTools()
    {
        shapesLayout.setVisibility(View.INVISIBLE);
        //  colorpicker_bar.setVisibility(View.VISIBLE);
        pen.setLayoutParams(paint_penpassive);
        marker.setLayoutParams(paint_penpassive);
        erase.setLayoutParams(paint_penpassive);
        select_path.setBackgroundResource(R.drawable.paint_crop_passive);
        shapes_btn.setBackgroundResource(R.drawable.paint_shape_passive);
        //   addshape_popup.setVisibility(View.GONE);
        //   drawshape_popup.setVisibility(View.GONE);

    }
    public static void penMarkerCondition()
    {
        draw_clone.getcountdraw(1);
        //  draw_clone.setErase(false);
        //  draw_clone.setNormal();
        //  iserase = false;
    }

    public static void colorPickerOnclick()
    {
        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw_clone.setpaintcolors(colorPicker.getColor());
                if (!draw_clone.stop_paint) {
                    draw_clone.isStart_paint = true;
                }
                if (draw_clone.stop_paint) {
                    draw_clone.isStart_paint = false;
                }
            }
        });

    }

    public static void seekbarOnclick()
    {
        seek_brush_size.setProgress(5);
        draw_clone.setPaintStrokeWidth(10);
        seek_brush_size.setMax(20);
       /*
        // done by anitha
        seek_brush_size.setProgress(20);
        draw_clone.setPaintStrokeWidth(40);
         seek_brush_size.setMax(100);
         */
        seek_brush_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int count = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                count = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            /*
             // done by anitha
             draw_clone.setPaintStrokeWidth(count * 2);
                strokeWidthPaint = count * 2;*/

                draw_clone.setPaintStrokeWidth(count * 2);
                strokeWidthPaint = count * 2;


            }
        });
    }

    public static String captureCanvasScreen()
    {
        View content = layBg;
        content.setDrawingCacheEnabled(true);
        content.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = content.getDrawingCache();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path,"EvernoteClone/Pictures");
        if(!file.exists()){
            file.mkdirs();
        }
        String file_path=file+"/"+"paint_"+System.currentTimeMillis()+".png";
        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.flush();
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_path;
    }

    public void saveAndIntent()
    {
        String path_noshape=new String();
        String path_withshape=new String();

        if(draw_clone.isPainted && count_ofShapes <= 1)// either paint or shape
        {
            path_withshape = path_noshape = captureCanvasScreen();
            System.out.println("only_path "+" path_withshape "+path_withshape+" path_noshape "+path_noshape);
        }
        else if(!draw_clone.isPainted && count_ofShapes > 1) //not shape and paint
        {
            path_withshape = path_noshape = captureCanvasScreen();
            System.out.println("only_sahpe "+" path_withshape "+path_withshape+" path_noshape "+path_noshape);
        }
        else  if(draw_clone.isPainted && count_ofShapes > 1) //both shape and paint
        {
            path_withshape = captureCanvasScreen();
            disables();
            path_noshape = captureCanvasScreen();
            System.out.println("shape_path "+" path_withshape "+path_withshape+" path_noshape "+path_noshape);
        }
        else{
            path_withshape = path_noshape = captureCanvasScreen();
        }

        if(getvalue==0) {
            Log.v("paint_inside","the_getOne");
            Intent i = new Intent(PaintModule.this, MainActivity.class);
            i.putExtra("shapesDraw", "shapesDraw");
            i.putExtra("byteArray", path_withshape);
            i.putExtra("withoutshape",path_noshape);
            setResult(3, i);
          //  overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        else{
          /*
            paintRepo.updateuri(getvalue,path_withshape);
            paintRepo.updateNoShapes(getvalue,path_noshape);*/


          /*
            String filename = FilenameUtils.getName(noviewpath);
            paintAttachment.setPaintPath(fullpath);
            paintAttachment.setPaint_uri(noviewpath);
            paintAttachment.setDocument_id(documentid);
            paintAttachment.setStatus("xx");
            paintAttachment.setServer_doc_id(-1);
            paintAttachment.setPaintName(filename);
            Long id = paintRepo.insert(paintAttachment);
            Log.d("paint_module idsss=", String.valueOf(id));

            Intent i = new Intent(PaintModule.this, NoteProMainActivity.class);
            startActivity(i);*/

          ////////////

            Log.d("paint_values_id", String.valueOf(documentid)+"title="+docTitle);
            Intent i = new Intent(PaintModule.this, MainActivity.class);
            i.putExtra("document_id",documentid);
            i.putExtra("title_for_document",docTitle);
            i.putExtra("shapesDraw", "shapesDraw");
            i.putExtra("byteArray", path_withshape);
            i.putExtra("withoutshape",path_noshape);
            setResult(3, i);
           // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        }
        deleteCroppedImages();
    }

    public void deleteCroppedImages()
    {
        for(int i=path_beforecropPaste.size();i>0;i--)
        {
            deleteImage(path_beforecropPaste.get(0));
            path_beforecropPaste.remove(path_beforecropPaste.get(0));
        }
        for(int i=path_aftercropPaste.size();i>0;i--)
        {
            deleteImage(path_aftercropPaste.get(0));
            path_aftercropPaste.remove(path_aftercropPaste.get(0));
        }

        if((!path_withview.isEmpty()) && (!path_noview.isEmpty()))
        {
            deleteImage(path_withview);
            deleteImage(path_noview);
        }
    }

    public void intentFromTextnotepage()
    {
        Intent intent = getIntent();
        try {
            int s1 = (intent.getExtras().getInt("ImageUri"));
            getvalue = s1;
            path_noview = (intent.getExtras().getString("Canvas_path_noview"));
            path_withview = (intent.getExtras().getString("Canvas_path_withview"));
            Log.v("path_witoutview",path_noview);

            if (path_noview != null) {
                System.out.println("path not null");
                if (path_noview.matches(path_withview)) {
                    System.out.println("two paths r same");
                    getCountOfAddedShapes();
                    if (count_ofShapes == 1) {
                        draw_clone.getpaths(path_noview);
                        System.out.println("only paint no path");
                    } else {
                        draw_clone.getpathnull();
                        System.out.println("only shape no paint");
                    }
                } else {
                    draw_clone.getpaths(path_noview);
                    System.out.println("path and view");
                }
            }
            if(noviewpath!= null)
            {
                draw_clone.getpaths(noviewpath);
                System.out.println("only paint no path");
            }
            else  if (noviewpath == null) {
                draw_clone.getpathnull();
                System.out.println("path is null");
            }

//            Log.v("findpath",path_withview);
//            draw_clone.getpaths(path_withview);
//            if(path_withview==null){
//                draw_clone.getpathnull();
//            }
        } catch (Exception e) {
        }
    }

    public int screendimension()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        Log.i("MY", "Actual Screen Height = " + screenHeight + " Width = " + screenWidth);
        return screenHeight;
    }


    public static void setUndoActive()
    {
        undo.setBackgroundResource(R.drawable.paint_undo_active);
    }

    public static void setUndoPassive()
    {
        undo.setBackgroundResource(R.drawable.paint_undo_passive);

    }

    public static void setRedoActive()
    {
        redo.setBackgroundResource(R.drawable.paint_redo_active);

    }

    public static void setRedoPassive()
    {
        redo.setBackgroundResource(R.drawable.paint_redo_passive);

    }
}

