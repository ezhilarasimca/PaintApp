package com.bloby.blob.paintapp;

/**
 * Created by SIT on 16/01/2018.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;



import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaintModuleCanvas extends View  {

    Path crop_path;
    private Path current_path;
    Path _path;
    Path selected_path;

    Paint crop_paint;
    private Paint bitmapPaint;
    private Paint current_paint,bitPaint,dummyPaint;
    Paint selected_path_paint;
    private Paint paint9;


    public ArrayList<Float> pointsx = new ArrayList<>();
    public ArrayList<Float> pointsy = new ArrayList<Float>();
    private ArrayList<Float> pointx = new ArrayList<Float>();
    private ArrayList<Float> pointy = new ArrayList<Float>();
    private ArrayList<Float> crop_pointx = new ArrayList<Float>();
    private ArrayList<Float> crop_pointy = new ArrayList<Float>();

    private ArrayList<Path> completePath_undo = new ArrayList<Path>();
    private ArrayList<Path> drawShapeEnabledPaths_undo = new ArrayList<Path>();
    private ArrayList<Path> normalPath_undo = new ArrayList<Path>();
    private ArrayList<Path> selectBtn_deletedPaths_undo = new ArrayList<Path>();
    private ArrayList<Path> pathsRemovedOrAdded_by_undo = new ArrayList<Path>();
    private ArrayList<Path> selectBtn_deletedPaths_redo = new ArrayList<Path>();
    private ArrayList<Path> drawShapeEnabledPaths_redo = new ArrayList<Path>();
    private ArrayList<Path> normalPath_redo = new ArrayList<Path>();
    private ArrayList<Path> path_list = new ArrayList<Path>();
    private ArrayList<Path> redoPathList = new ArrayList<Path>();

    private ArrayList<Path> undonePaths = new ArrayList<Path>();
    private ArrayList<Path> removed_paths = new ArrayList<Path>();

    private ArrayList<Paint> undo_paints = new ArrayList<Paint>();
    private ArrayList<Paint> redo_paints = new ArrayList<Paint>();
    private ArrayList<Paint> paint_list = new ArrayList<>();
    private ArrayList<Paint> redoPaintList = new ArrayList<>();

    private ArrayList<Paint> mUndonePaints = new ArrayList<>();
    private ArrayList<Paint> removed_paints = new ArrayList<>();

    ArrayList<String> cropped_path = new ArrayList<String>();
    ArrayList<String> redo_croppedpath = new ArrayList<String>();
    ArrayList<String> undo_croppedpath = new ArrayList<String>();

    ArrayList<Integer> stop_undopath = new ArrayList<Integer>();
    ArrayList<Integer> stop_redopath = new ArrayList<Integer>();
    ArrayList<Integer> clr_pathSize = new ArrayList<Integer>();
    ArrayList<Path> remove_Crop = new ArrayList<Path>();


    public static List<Point> points;
    Point mlastpoint = null;

    Context context;

    public Bitmap bitmap;
    Bitmap second_bitmap;

    private Canvas mcanvas;


    public Paint.Style style1 = Paint.Style.STROKE;

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    float _startx, _starty, _endx, _endy;
    float radius_circlex;
    float rect_left_min_x, rect_top_min_y, rect_right_max_x, rect_bottom_max_y;
    float x_circle, y_circle;
    float tri_startx, tri_starty, tri_midx, tri_midy, tri_endx, tri_endy;

    String cropped_mainCanvas_path;
    String oldMainCanvas_path;
    String path_got;

    int viewheight=(int)(0.3*screendimension());

    int someamt=(viewheight/2);
    int reduceamt=(someamt+76);
    int finalreduceamt=viewheight-reduceamt;
    int drawview_count = 0;
    int y1=viewheight;
    int y2=viewheight;
    int x_vline=viewheight/2-viewheight/7;
    int y_hline=viewheight/2-viewheight/7;
    int x_sq=viewheight/2+viewheight/4;
    int y_sq=viewheight/2+viewheight/4;
    int x_circle1=(viewheight-190)/2,y_circle1=(viewheight-190)/2,radius_circle=(viewheight-190)/2;
    int drawviewdraw=0;

    int y,x=200;
    int x_tri,y_tri=viewheight-190;
    int width=viewheight-190;
    int height_he=viewheight-190,width_he=viewheight-190;
    int height_pen=viewheight-190,width_pen=viewheight-190;
    /* int y1 = 500;
     int y2 = 500;
     int x_vline = 500 / 2 - 500 / 7;
     int y_hline = 500 / 2 - 500 / 7;
     int x_sq = 500 / 2 + 500 / 4;
     int y_sq = 500 / 2 + 500 / 4;
     int x_circle1 = 500 / 2 - 500 / 10, y_circle1 = 200, radius_circle = 200 / 2;
     int drawviewdraw = 0;
     int y, x = 500 / 3;
     int x_tri, y_tri = 500 - 500 / 3;
     int width = 500 - 500 / 3;
     int height_he = 500 - 500 / 4, width_he = 500 - 500 / 4;
     int height_pen = 500 - 500 / 4, width_pen = 500 - 500 / 4;*/
    int tri_selected_count, circle_selected_count, rect_selected_count, line_selected_count;
    int paralle_selected_count;
    int _selectedpath_index;
    int index_min_pointsy, index_max_pointsy;
    int index_min_pointsx, index_max_pointsx, poinsx_difference,poinsy_difference;
    int point_top, point_left, point_right;
    public int mBackgroundColor = Color.TRANSPARENT;
    private int mPaintColor = Color.BLACK;
    private int mPaintColor1 = Color.BLACK;
    private int mStrokeWidth = 15;
    private int mnew_paintcolor = Color.TRANSPARENT;
    public int mstrokewidth = 5;
    int paint_alpha = 255;


    boolean h_line = false;
    boolean v_line = false;
    boolean s_line = false;
    boolean t_line = false;
    boolean he_line = false;
    boolean pen_line = false;
    boolean circle_line = false;
    Boolean isUndo = false, isRedo;
    Boolean wassecondbitmap_deleted = false;
    Boolean issecondbitmap_invisible = false;
    boolean isNormal = true, isEmboss = false, isBlur = false, isSmudge = false,
            isDashed = false, isComposedPath = false, isTrianglePath = false;
    private boolean setErase = false;
    boolean isSrcATop = false;
    Boolean draw_line = false;
    Boolean draw_circle = false;
    Boolean draw_rectangle = false;
    Boolean draw_triangle = false;
    Boolean line_selected = false, circle_selected = false, rect_selected = false, triangle_selected = false;
    Boolean dashed_line = false;
    Boolean ispath_got = false;
    boolean bfirstpoint = false;
    Boolean was_cropped = false;
    Boolean isPainted = false;
    Boolean ismarker = false, ispen = true;
    Boolean isStart_paint = false;
    Boolean stop_paint = false, candeleteimage = false;
    Boolean crop_intersect = false, crop_differecepart = false, load_croppedbitmap = false,
            clear_paths = false, iscrop = false;
    Boolean candrawShapes=false;
    Boolean canCrop = false;

    int normalCount=0,retrieveCount=0;

    /////done by ezhil///////

    Bitmap bm,aa,bb;
    Canvas canva;
    boolean canva_bool=true,cropClick=false;
    boolean flgPathDraw1 = false;
    Point mlastpoint1 = null;
    boolean bfirstpoint1 = false,resultingval=false,ptClick=false,callbitmap=false,points_draw=false,src_out=false;
    Point mfirstpoint = null;
    Context mContext;
    Bitmap res_bitmap,partial_bitmap;
    boolean check_crop_val=false;

   // ArrayList<PaintDrawModel> allList=new ArrayList<PaintDrawModel>();


    public static List<Integer> pointx1;
    public static List<Integer> pointy1;
    List<Point> points_replace;
    int width1,height1=0;
    int btX,btY=10;
    int bt_startX,bt_startY,bt_endx,bt_endy;

    boolean ctrl_crop=true;


    public PaintModuleCanvas(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        setLayerType(LAYER_TYPE_SOFTWARE,null);
        this.context=context;
        mContext=context;
        pointx1=new ArrayList<Integer>();
        pointy1=new ArrayList<Integer>();
        points=new ArrayList<Point>();
        points_replace=new ArrayList<Point>();
        initPath();
        paint9 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint9.setStyle(Paint.Style.STROKE);
        paint9.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        paint9.setStrokeWidth(5);
        paint9.setColor(Color.WHITE);
        bfirstpoint = false;

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        width1=w;
        height1=h;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(bitmap);
    }
    private void initPath()
    {
        current_path = new Path();
        selected_path=new Path();
        _path=new Path();
        bitmapPaint = new Paint();
        selected_path_paint = new Paint();
        crop_path=new Path();
        this.initPaint();
    }
    private void initPaint()
    {
        System.out.println("initpaint is called");
        current_paint = new Paint();
        current_paint.setAntiAlias(true);
        current_paint.setStyle(Paint.Style.STROKE);
        current_paint.setStrokeJoin(Paint.Join.ROUND);
        current_paint.setStrokeCap(Paint.Cap.ROUND);
        current_paint.setStrokeWidth(mStrokeWidth);
        bitPaint = new Paint();
        bitPaint.setAntiAlias(true);
        bitPaint.setStyle(Paint.Style.STROKE);
        bitPaint.setStrokeJoin(Paint.Join.ROUND);
        bitPaint.setStrokeCap(Paint.Cap.ROUND);
        bitPaint.setStrokeWidth(mStrokeWidth);

       /* dummyPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        dummyPaint.setStyle(Paint.Style.STROKE);
        dummyPaint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
        dummyPaint.setStrokeWidth(5);
        dummyPaint.setColor(Color.GRAY);*/

        if(!setErase) {
            current_paint.setColor(mPaintColor);
            current_paint.setAlpha(paint_alpha);
        }
        if(setErase)
        {
            System.out.println("eraser is active");
            current_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        if(iscrop)
        {
            System.out.println("crop is active");
            current_paint.setStrokeWidth(5);
            current_paint.setColor(Color.BLACK);
            current_paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
        }

            if(cropClick==true){
                Log.v("cropClick","arewroking");
                current_paint=new Paint(Paint.ANTI_ALIAS_FLAG);
                current_paint.setStyle(Paint.Style.STROKE);
                current_paint.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
                current_paint.setStrokeWidth(5);
                current_paint.setColor(Color.GRAY);

            }
        invalidate();

    }
    private void drawBackground(Canvas canvas)
    {
        bitmapPaint.setColor(Color.TRANSPARENT);
        bitmapPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), bitmapPaint);
    }

    private void drawPaths(Canvas canvas)
    {
        int i = 0;
        for (Path p : path_list) {
            Log.v("path_list_are", String.valueOf(path_list.size()));
            canvas.drawPath(p, paint_list.get(i));
            i++;
        }
    }
    public  void clipCanvasForCrop()
    {
        crop_intersect=true;
        crop_differecepart=false;
        invalidate();
    }

    public void getMainCanvasCroppedPath(String current_path)
    {
        oldMainCanvas_path=current_path;
        crop_intersect=false;
        cropped_mainCanvas_path=current_path;
        load_croppedbitmap=true;
        clear_paths=true;
        invalidate();
    }
    public void getMainCanvasPathAfterCrop(String oldPath,String newpath)
    {
        candeleteimage=true;
        oldMainCanvas_path=oldPath;
        crop_intersect=false;
        cropped_mainCanvas_path=newpath;
        load_croppedbitmap=true;
        clear_paths=true;
        invalidate();
    }
    public void getViewCanvasCroppedPath()
    {
        crop_differecepart=true;
        invalidate();

    }
    public void disableCropPaintColor()
    {
        paint_list.get(paint_list.size()-1).setColor(Color.TRANSPARENT);
        invalidate();
    }
/*
    public void initiateCrop()
    {
        iscrop=true;
        invalidate();
    }
*/
    public void initiateCrop()
    {
        iscrop=true;
        invalidate();
    }
    public void deleteFile(String current_path)
    {
        File fdelete = new File(current_path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {}
            else {}
        }
    }

    public void disableCrop()
    {
        iscrop=false;
    }
    @Override
    protected void onDraw(Canvas canvas)
    {


        drawBackground(canvas);



        double screenData = getScreenDimension();

        Log.d("screen_dimension__", String.valueOf(screenData));
//ondraw is called in 283 viewheight 717 (black big nexus) //4.99 inches //eight = 2392 Width = 1440

//ondraw is called in 190 viewheight 532 (small nexus) //4.33 inches //= 1776 Width = 1080

//ondraw is called in 201 viewheight 554 (tab) //9.18 inches //Height = 1848 Width = 1200



        if((screenData==4.33)) //nexus 4
        {

            if(normalCount==0){

                normalCount=1;
            }


        }

      else  if((screenData==4.99)) //big nexus
        {

            if(normalCount==0){
                finalreduceamt=finalreduceamt-71;
                normalCount=1;
            }

        }

       else if((screenData==9.18)) //tab
        {

            if(normalCount==0){
                finalreduceamt=finalreduceamt-100;

                normalCount=1;
            }

        }

        ////

        if(normalCount==1 && retrieveCount==0){
            Log.v("insideAssign","valuessArethere");
            asignShapeValue();
            normalCount=2;
        }




        try {
            if (crop_differecepart) {
                // iscrop=false;
                canvas.clipPath(path_list.get(path_list.size() - 1), Region.Op.DIFFERENCE);
            }
        }
        catch (Exception e)
        {
            System.out.println("exception "+e);
        }
        try {
            if (crop_intersect) {
                //  iscrop=false;
                isStart_paint = false;
                stop_paint = true;
                canvas.clipPath(path_list.get(path_list.size() - 1), Region.Op.INTERSECT);
            }
        }
        catch (Exception e)
        {
            System.out.println("exception "+e);
        }


/*
        try {
            if (load_croppedbitmap) {
                crop_differecepart = false;
                crop_intersect = false;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                second_bitmap = BitmapFactory.decodeFile(cropped_mainCanvas_path, options);
                Rect rectangle = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                canvas.drawBitmap(second_bitmap, null, rectangle, null);
                if (clear_paths) {
                    path_list.clear();
                    paint_list.clear();
                    clear_paths = false;
                }
                if (candeleteimage) {
                    deleteFile(oldMainCanvas_path);
                    candeleteimage = false;
                }
            } else if (ispath_got == true) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                second_bitmap = BitmapFactory.decodeFile(path_got, options);
                Rect rectangle = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                canvas.drawBitmap(second_bitmap, null, rectangle, null);
            }
        }
        catch (Exception e)
        {
            System.out.println("exception "+e);
        }
*/
        canvas.drawBitmap(bitmap,0,0,bitPaint);
        if (ispath_got == true) {
            ispath_got=false;
            Log.v("is_path_got_is_","true");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap  second_bitmap1 = BitmapFactory.decodeFile(path_got, options);
           /* Rect rectangle = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(second_bitmap, null, rectangle, null);*/
            merging(bitmap,second_bitmap1,bitmap.getWidth(),bitmap.getHeight());
            bitmap=second_bitmap1;
            canvas.drawBitmap(bitmap,0,0,bitPaint);
            changePaint();
            invalidate();
        }

        try {
            drawPaths(canvas);
        }
        catch (Exception e)
        {
            System.out.println("exception "+e);
        }
        System.out.println("cropClicj_set "+cropClick);
        if(cropClick==true) {
            Path path = new Path();
            boolean first = true;


            for (int i = 0; i < points.size(); i += 2) {
                Log.v("points_are", "indraw");
                Point point = points.get(i);
                if (first) {
                    first = false;
                    path.moveTo(point.x, point.y);
                } else if (i < points.size() - 1) {
                    Point next = points.get(i + 1);
                    path.quadTo(point.x, point.y, next.x, next.y);
                } else {
                    mlastpoint1 = points.get(i);
                    path.lineTo(point.x, point.y);
                }

            }

            Log.v("crop_path_are","inside");
            canvas.drawPath(path, current_paint);
           /* remove_Crop.add(path);
            resetPath(path,canvas);*/


        }
        if(callbitmap==true){
            callbitmap=false;
            bitmapFunction(1);
        }

        if(src_out==true){
            src_out=false;
            bitmapFunctionSrc(2);
            bitmapFunction(1);
            bitmap=partial_bitmap;
            invalidate();
        }

        if(resultingval==true){

            Log.v("canvas_Rewrite","img");
            canvas.drawBitmap(res_bitmap,btX,btY,current_paint);
            ptClick=true;
        }

       /* try{
            paint9 = new Paint();
            paint9.setStyle(Paint.Style.STROKE);
            paint9.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));
            paint9.setStrokeWidth(5);
            paint9.setColor(Color.BLACK);
            Path path9 = new Path();
            boolean first = true;
            for (int i = 0; i < points.size(); i += 2) {
                Point point = points.get(i);
                if (first) {
                    first = false;
                    path9.moveTo(point.x, point.y);
                } else if (i < points.size() - 1) {
                    Point next = points.get(i + 1);
                    path9.quadTo(point.x, point.y, next.x, next.y);
                } else {
                    mlastpoint = points.get(i);
                    path9.lineTo(point.x, point.y);
                }
            }
            canvas.drawPath(path9, paint9);}catch(Exception w){}*/
        Paint newpaint=new Paint();
        Paint newpaint1=new Paint();
        newpaint.setColor(mPaintColor1);
        newpaint.setStrokeWidth(mstrokewidth);
        newpaint.setStyle(style1);
        newpaint.setStrokeJoin(Paint.Join.ROUND);
        newpaint.setStrokeCap(Paint.Cap.ROUND);
        newpaint1.setColor(mnew_paintcolor);

        newpaint1.setStyle(Paint.Style.FILL);
        newpaint1.setStrokeJoin(Paint.Join.ROUND);
        newpaint1.setStrokeCap(Paint.Cap.ROUND);
        if(drawviewdraw==1){
            current_path.moveTo(pointx.get(0),pointy.get(0));
            for(int i=1;i<pointx.size();i++){
                current_path.lineTo(pointx.get(i),pointy.get(i));
                canvas.drawPath(current_path,current_paint);
            }
        }

        if(dashed_line)
        {
            drawOuterLine(canvas);
        }


        if(v_line==true){
            Path _path=new Path();
            _path.moveTo(x_vline,10);
            _path.lineTo(x_vline,y1);
            canvas.drawPath(_path, newpaint);
        }
        if(h_line==true){
            Path _path=new Path();
            _path.moveTo(0,y_hline);
            _path.lineTo(y2-10,y_hline);
            canvas.drawPath(_path, newpaint);
        }
        if(s_line==true){
            canvas.drawRect(25, 25, x_sq-50, y_sq-50, newpaint);
            canvas.drawRect(25, 25, x_sq-50, y_sq-50, newpaint1);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }
        if(t_line==true) {
            Path _path=new Path();
            _path.moveTo(width/2, 8);
            _path.lineTo(width, y_tri);
            _path.lineTo(width/18,y_tri);
            _path.moveTo(width/2,8);
            _path.lineTo(width/18,y_tri);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }
        if(he_line==true){
            Path _path=new Path();
    /*_path.moveTo(width_he/5,15);
    _path.lineTo(width_he/4+width_he/3+width_he/10,15);
    _path.lineTo(width_he-width_he/7,height_he/2-height_he/10);
    _path.lineTo(width_he-width_he/3,height_he-height_he/4);
    _path.lineTo(width_he-(width_he/3+width_he/2)+width_he/9,height_he-height_he/4);
    _path.lineTo(width_he/11,height_he-(height_he/2+height_he/15));
    _path.moveTo(width_he/4+8,15);
    _path.lineTo(width_he/11,height_he-(height_he/2+height_he/15));*/
            float radi=height_he/2-10;
            Log.v("widthhex",width_he+"height"+height_he);

            float triangleHeight = (float) (Math.sqrt(3) * radi/ 2);
            float centerX = width_he/2;
            float centerY = height_he/2;
            _path.moveTo(centerX, centerY + radi);
            _path.lineTo(centerX - triangleHeight, centerY + radi/2);
            _path.lineTo(centerX - triangleHeight, centerY - radi/2);
            _path.lineTo(centerX, centerY - radi);
            _path.lineTo(centerX + triangleHeight, centerY - radi/2);
            _path.lineTo(centerX + triangleHeight, centerY + radi/2);
            _path.lineTo(centerX, centerY + radi);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }

        if(pen_line==true){
            Path _path=new Path();
            _path.moveTo(width_pen/2, 10);
            _path.lineTo(width_pen, height_pen/3+(height_pen/10));
            _path.lineTo(width_pen/2+width_pen/3,height_pen);
            _path.lineTo(width_pen/5,height_pen);
            _path.lineTo(20,height_pen/2-20);

            _path.lineTo(width_pen/2, 10);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }

        if(circle_line==true){
            Path _path=new Path();
   /* canvas.drawCircle(x_circle1,y_circle1,radius_circle+20,newpaint);
    canvas.drawCircle(x_circle1,y_circle1,radius_circle+20,newpaint1);*/
            //canvas.translate(310/2f,310/2f);
            canvas.drawCircle(x_circle1,y_circle1, radius_circle-10, newpaint);
            canvas.drawCircle(x_circle1,y_circle1, radius_circle-10, newpaint1);
        }

      /*  if(v_line==true){
            Path _path=new Path();
            _path.moveTo(x_vline,10);
            _path.lineTo(x_vline,y1);
            canvas.drawPath(_path, newpaint);
        }
        if(v_line==true){
            Path _path=new Path();
            _path.moveTo(x_vline,10);
            _path.lineTo(x_vline,y1);
            canvas.drawPath(_path, newpaint);
        }
        if(h_line==true){
            Path _path=new Path();
            _path.moveTo(0,y_hline);
            _path.lineTo(y2-10,y_hline);
            canvas.drawPath(_path, newpaint);
        }
        if(s_line==true){
            canvas.drawRect(25, 25, x_sq-50, y_sq-50, newpaint);
            canvas.drawRect(27, 27, x_sq-51, y_sq-51, newpaint1);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }
        if(t_line==true) {
            Path _path=new Path();
            _path.moveTo(x, 0);
            _path.lineTo(width-30, y_tri-40);
            _path.lineTo(width-(width-30),y_tri-40);
            _path.moveTo(x,0);
            _path.lineTo(width-(width-30),y_tri-40);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }
        if(he_line==true){
            Path _path=new Path();
            _path.moveTo(width_he/4+8,15);
            _path.lineTo(width_he/4+width_he/3+width_he/10,15);
            _path.lineTo(width_he-width_he/7,height_he/2-height_he/10);
            _path.lineTo(width_he-width_he/3,height_he-height_he/4);
            _path.lineTo(width_he-(width_he/3+width_he/2)+width_he/9,height_he-height_he/4);
            _path.lineTo(width_he/11,height_he-(height_he/2+height_he/15));
            _path.moveTo(width_he/4+8,15);
            _path.lineTo(width_he/11,height_he-(height_he/2+height_he/15));
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }

        if(pen_line==true){
            Path _path=new Path();
            _path.moveTo(width_pen/2-8, 10);
            _path.lineTo(width_pen/13, height_pen/3);
            _path.lineTo(width_pen/6,height_pen-height_pen/5);
            _path.lineTo(width_pen-width_pen/4,height_pen-height_pen/5);
            _path.lineTo(width_pen-width_pen/6,height_pen/7+height_pen/5);
            _path.moveTo(width_pen-width_pen/6,height_pen/7+height_pen/5);
            _path.lineTo(width_pen/2-8, 10);
            canvas.drawPath(_path, newpaint);
            canvas.drawPath(_path, newpaint1);
        }

        if(circle_line==true){
            Path _path=new Path();
            canvas.drawCircle(x_circle1,y_circle1,radius_circle,newpaint);
            canvas.drawCircle(x_circle1,y_circle1,radius_circle,newpaint1);
        }*/
        try {
            if(cropClick!=true) {
                Log.v("values_are","called_here");
                canvas.drawPath(current_path, current_paint);
            }
        }
        catch (Exception e)
        {
            System.out.println("exception "+e);
        }

    }


    public void setCountt(){
        Log.v("countArese","in that place");
        retrieveCount=2;
    }

    public void asignShapeValue(){

        x_circle1=(viewheight-finalreduceamt)/2;y_circle1=(viewheight-finalreduceamt)/2;radius_circle=(viewheight-finalreduceamt)/2;


        y=200;x=200;
        x_tri=viewheight-finalreduceamt;y_tri=viewheight-finalreduceamt;
        width=viewheight-finalreduceamt;
        height_he=viewheight-finalreduceamt;width_he=viewheight-finalreduceamt;
        height_pen=viewheight-finalreduceamt;width_pen=viewheight-finalreduceamt;
    }

    /////////////////////////////////////////////////////////////
    //////////////////////free hand sketch//////////////////////
    //////////////////////////////////////////////////////////

    public void setPaintStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        current_paint.setStrokeWidth(mStrokeWidth);
    }

    public void initiatePen()
    {
        disableAll();
        setPaintAlpha255();
        invalidate();
    }
    public void initiateMarker()
    {
        disableAll();
        setPaintAlpha90();
        invalidate();
    }

    public void initiateEraser()
    {
        disableAll();
        activateEraser();
        invalidate();
    }
    public void setPaintAlpha255()
    {
        paint_alpha=(255);
        invalidate();
    }
    public void setPaintAlpha90()
    {
        paint_alpha=(90);
        invalidate();
    }
    public void activateEraser()
    {
        setErase = true;
        invalidate();
    }
    ////////////////////////////////////////////////////////
    //////////////////ends free hand sketch ////////////////
    /////////////////////////////////////////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int _x = (int)event.getX();
        int _y = (int)event.getY();
       /* int pixel = bitmap.getPixel(_x,_y);

        System.out.println("pixel in ontouch"+pixel);
        int red = Color.red(pixel);
        int blue = Color.blue(pixel);
        int green = Color.green(pixel);
        System.out.println("green "+green+"blue "+blue+"red "+red);*/
       /* if(iscrop)
        {
            current_paint.setStrokeWidth(5);
            current_paint.setColor(Color.BLACK);
        }*/
        initPaint();
        float eventX = event.getX();
        float eventY = event.getY();
        pointsx.add(eventX);
        pointsy.add(eventY);
        crop_pointy.add(eventY);
        crop_pointx.add(eventX);

        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        if (flgPathDraw1) {
            //  ptclick=false;
            Log.v("flgPathDraw1","inside");

            if (bfirstpoint1) {

                if (comparepoint(mfirstpoint, point)) {
                    points.add(mfirstpoint);
                    pointx1.add(mfirstpoint.x);
                    pointy1.add(mfirstpoint.y);
                    flgPathDraw1 = false;
                    showcropdialog();
                } else {
                    points.add(point);
                    pointx1.add(point.x);
                    pointy1.add(point.y);
                }
            } else {
                points.add(point);
                pointx1.add(point.x);
                pointy1.add(point.y);
            }

            if (!(bfirstpoint1)) {

                Log.v("bPoint","aretrue");
                pointx1.add(point.x);
                pointy1.add(point.y);
                mfirstpoint = point;
                bfirstpoint1 = true;
            }
        }

        System.out.println("ontouch event is called");
        if (isStart_paint) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("action down is called");
                    if(ptClick==true){
                        Log.v("action_sown","are ptclick");
                    }
                    else {
                        pointsx.clear();
                        pointsy.clear();
                        crop_pointx.clear();
                        crop_pointy.clear();
                        current_path.moveTo(eventX, eventY);
                        lastTouchX = eventX;
                        lastTouchY = eventY;
                        invalidate();
                    }
                    return true;
                case MotionEvent.ACTION_MOVE:
                    System.out.println("action move is called");
                    if(ptClick==true){
                    /*Log.v("left_values",left1+"top_val"+top1);*/
                   /* btX= (int) (event.getX()-left1);
                    btY= (int) (event.getY()-top1);
                    bt_startX=btX;
                    bt_startY=btY;*/
                        btX= (int) (eventX-(bt_startX));
                        btY= (int) (eventY-(bt_startY));
                        Log.v("move_event_are"+eventX+"yyy"+eventY,btX+"yvakky"+btY);
                    }
                    else {
                        resetDirtyRect(eventX, eventY);
                        int historySize = event.getHistorySize();
                        for (int i = 0; i < historySize; i++) {
                            float historicalX = event.getHistoricalX(i);
                            float historicalY = event.getHistoricalY(i);
                            expandDirtyRect(historicalX, historicalY);
                            current_path.lineTo(historicalX, historicalY);
                        }
                        //current_path.lineTo(eventX, eventY);
                        invalidate();
                    }
                    return true;
                case MotionEvent.ACTION_UP:

                    mlastpoint1 = point;
                /*if(ptClick==true){

                }*/
                    if (flgPathDraw1) {
                        Log.v("they_are_in","action_up");
                        if (points.size() > 12) {
                            if (!comparepoint(mfirstpoint, mlastpoint1)) {
                                pointx1.add(mfirstpoint.x);
                                pointy1.add(mfirstpoint.y);
                                points.add(mfirstpoint);
                                flgPathDraw1 = false;
                                invalidate();
                                Log.v("they_are_in","action_up22");
                                showcropdialog();
                            }
                        }
                    }
                    else if(ptClick==true){
                        Log.v("here_actionup","occur");
                        ptClick=false;
                        resultingval=false;
                        ctrl_crop=true;
                        current_path=new Path();
                        initPaint();
                        merging(bitmap,res_bitmap,btX,btY);

                    }

                    else {
                        current_path.lineTo(eventX, eventY);
                        addPathAndPaintToList();
                        System.out.println("paint_alpha " + paint_alpha);
                        updateUndoRedoPathsForNormalPaintOption();
                        initiatePathAndPaint();
                        if (candrawShapes) drawShapes();

                        initPaint();
                        invalidate();
                    }
                    return true;
            }
            invalidate();
        }

        return true;
    }
    private void expandDirtyRect(float historicalX, float historicalY) {
        try{
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }
            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }
        catch (Exception e)
        {

        }
    }
    private void resetDirtyRect(float eventX, float eventY) {
        try{
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
        catch (Exception e)
        {

        }
    }

    public void clearPaint()
    {
        try {
            path_list.clear();
            paint_list.clear();
            undonePaths.clear();
            mUndonePaints.clear();
            completePath_undo.clear();
            undo_paints.clear();
            pathsRemovedOrAdded_by_undo.clear();
            redo_paints.clear();

            if (was_cropped) {
                was_cropped = false;
            }
            ispath_got = false;
            mcanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            isPainted = false;
        }
        catch (Exception e)
        {

        }
        invalidate();
    }

    public void resetpaint()
    {
        try {
            path_list.clear();
            paint_list.clear();
            undonePaths.clear();
            mUndonePaints.clear();
            completePath_undo.clear();
            undo_paints.clear();
            pathsRemovedOrAdded_by_undo.clear();
            redo_paints.clear();
        }
        catch(Exception e)
        {

        }
    }
    public void setPaintColor(int color) {
        mPaintColor1 = color;
        invalidate();
    }
    public void setpaintdb(int color){
        mPaintColor1=color;
    }
    public void setnewpaint(int color){
        mnew_paintcolor=color;
        invalidate();
    }

    public void setnewpaint1(int color){
        mnew_paintcolor=color;
    }

    public void setnewstroke(int i){
        mstrokewidth=i;
        invalidate();
    }
    public void setnewstrokewidth(int i){
        mstrokewidth=i;
    }

    public void setpaintcolors(int color){
        mPaintColor=color;
        if(!setErase) {
            current_paint.setColor(mPaintColor);
        }
        if(setErase)
        {
            current_paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        invalidate();
    }


    public void getcountdraw(int i){
        drawview_count=i;
        mBackgroundColor=Color.TRANSPARENT;
        current_paint.setColor(Color.BLACK);
        invalidate();
    }



    public Bitmap getBitmap()
    {
        try{
            mcanvas=new Canvas(bitmap);
            drawBackground(mcanvas);
            drawPaths(mcanvas);
        }
        catch (Exception e){
            Bitmap new_bit=bitmap;
            Bitmap workingBitmap = Bitmap.createBitmap(new_bit);
            bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
            mcanvas=new Canvas(bitmap);
            drawBackground(mcanvas);
            drawPaths(mcanvas);
        }

        return bitmap;
    }

    /////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////draw shapes////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    public void initiateDrawShapes()
    {
        try {
            candrawShapes = true;
            setPaintAlpha255();
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void drawShapes() {

        try {
            //calculate minimum value index and maximum value index from arraylist of pointsx and pointsy
            getDrawShapesMinMaxIndex();

            //line
            getDrawLinePoints();
            //rectangle
            getDrawRectPoints();
            //circle
            getDrawCirclePoints();
            //triangle
            getDrawTrianglePoints();

            //add drawn shapes path to path list
            drawShapesPath();
        }
        catch (Exception e)
        {

        }

        invalidate();
    }

    public void getDrawShapesMinMaxIndex()
    {
        try {
            index_min_pointsy = pointsy.indexOf(Collections.min(pointsy));
            index_max_pointsy = pointsy.indexOf(Collections.max(pointsy));
            poinsy_difference = (int) Math.abs(pointsy.get(index_max_pointsy)) - (int) Math.abs(pointsy.get(index_min_pointsy));
            index_min_pointsx = pointsx.indexOf(Collections.min(pointsx));
            index_max_pointsx = pointsx.indexOf(Collections.max(pointsx));
            poinsx_difference = (int) Math.abs(pointsx.get(index_max_pointsx)) - (int) Math.abs(pointsx.get(index_min_pointsx));
            poinsy_difference = (int) Math.abs(pointsy.get(index_max_pointsy)) - (int) Math.abs(pointsy.get(index_min_pointsy));
System.out.println("index_min_pointsy "+index_min_pointsy+"index_max_pointsy "+index_max_pointsy
+"index_min_pointsx "+index_min_pointsx+"index_max_pointsx "+index_max_pointsx+"pointsy size "+pointsy.size()
+"pointsx size "+pointsx.size());
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void getDrawLinePoints()
    {
        try {
            _startx = pointsx.get(index_min_pointsx);
            _starty = pointsy.get(index_min_pointsx);
            _endx = pointsx.get(index_max_pointsx);
            _endy = pointsy.get(index_max_pointsx);
            System.out.println("draw line _startx" + _startx + "_starty " + _starty + "_endx " + _endx + "_endy " + _endy);
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void getDrawRectPoints()
    {
        try {
            rect_left_min_x = pointsx.get(index_min_pointsx);
            rect_top_min_y = pointsy.get(index_min_pointsy);
            rect_right_max_x = pointsx.get(index_max_pointsx);
            rect_bottom_max_y = pointsy.get(index_max_pointsy);
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void getDrawTrianglePoints()
    {
        try{


            int diffY1 = (int)Math.abs((pointsy.get(index_min_pointsy))-(pointsy.get(index_min_pointsx)));
            int diffY2 = (int)Math.abs((pointsy.get(index_max_pointsy))-(pointsy.get(index_min_pointsx)));

            point_left=index_min_pointsx;
            point_right=index_max_pointsx;

            if(diffY1>diffY2)
            {
                point_top=index_min_pointsy;
            }
            else
            {
                point_top=index_max_pointsy;
            }
            tri_startx=pointsx.get(point_top);
            tri_starty=pointsy.get(point_top);
            tri_midx=pointsx.get(point_left);
            tri_midy=pointsy.get(point_left);
            tri_endx=pointsx.get(point_right);
            tri_endy=pointsy.get(point_right);

        }
        catch (Exception e)
        {

        }
        invalidate();
    }

/*
    public void getDrawTrianglePoints()
    {
        try{
            point_top=index_min_pointsy;
            point_left=index_min_pointsx;
            point_right=index_max_pointsx;
            System.out.println("point_top "+point_top+"point_left "+point_left+"point_right "+point_right+"index_max_pointsy"
            +index_max_pointsy);
            tri_startx=pointsx.get(point_top);
            tri_starty=pointsy.get(point_top);
            tri_midx=pointsx.get(point_left);
            tri_midy=pointsy.get(point_left);
            tri_endx=pointsx.get(point_right);
            tri_endy=pointsy.get(point_right);
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
*/
    public void getDrawCirclePoints()
    {
        try{
            if((poinsy_difference/2)>( poinsx_difference / 2))
            {
                radius_circlex = (poinsy_difference/2);
            }
            else
            {
                radius_circlex = poinsx_difference / 2;

            }

            y_circle = pointsy.get(index_min_pointsy)+radius_circlex;
            x_circle = pointsx.get(index_min_pointsx)+radius_circlex;
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void initiateDrawLine()
    {
        disableAll();
        selectLine();
        invalidate();
    }
    public void initiateDrawRectangle()
    {
        disableAll();
        selectRectangle();
        invalidate();
    }
    public void initiateDrawCircle()
    {
        disableAll();
        selectCircle();

        invalidate();
    }
    public void initiateDrawTriangle()
    {
        disableAll();
        selectTriangle();
        invalidate();
    }
    public void addPathAndPaintToList()
    {
        try{
            initPaint();
            path_list.add(current_path);
            paint_list.add(current_paint);
            //allList.add(new PaintDrawModel(bitmap,current_path,current_paint,false,true));

            if(!iscrop) {
                redoPaintList.clear();
                redoPathList.clear();
                PaintModule.setRedoPassive();
                PaintModule.setUndoActive();
            }
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void initiatePathAndPaint()
    {
        try{
            current_path = new Path();
            current_paint = new Paint();
        }
        catch (Exception e)
        {

        }
        invalidate();
    }

    public void constructLinePath()
    {
        try{
            current_path.moveTo(_startx, _starty);
            current_path.lineTo(_endx, _endy);
            current_path.close();
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void constructRectPath()
    {
        try{
            current_path.addRect(rect_left_min_x, rect_top_min_y, rect_right_max_x, rect_bottom_max_y, Path.Direction.CCW);
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void constructCirclePath()
    {
        try {
            current_path.addCircle(x_circle, y_circle, radius_circlex, Path.Direction.CW);
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void constructTrianglePath()
    {
        try{
            current_path.moveTo(tri_startx,tri_starty);
            current_path.lineTo(tri_midx,tri_midy);
            current_path.lineTo(tri_endx,tri_endy);
            current_path.lineTo(tri_startx,tri_starty);
            current_path.close();
        }
        catch (Exception e)
        {

        }
        invalidate();
    }
    public void selectLine()
    {
        line_selected=true;
        invalidate();
    }
    public void selectRectangle()
    {
        rect_selected=true;
        invalidate();
    }
    public void selectCircle()
    {
        circle_selected=true;
        invalidate();
    }
    public void selectTriangle()
    {
        triangle_selected=true;
        invalidate();
    }
    public void drawLinePath()
    {
        try{
            if(line_selected)
            {
                undoPathAndPaint();
                constructLinePath();
                addPathAndPaintToList();
                updateUndoRedoPathsWhenShapesEnabled();
                initiatePathAndPaint();
                System.out.println("line selected is called");
                invalidate();
            }
        }
        catch (Exception e)
        {

        }
    }
    public void drawRectanglePath()
    {
        try{
            if(rect_selected)
            {
                undoPathAndPaint();
                constructRectPath();
                addPathAndPaintToList();
                updateUndoRedoPathsWhenShapesEnabled();
                initiatePathAndPaint();
                invalidate();
            }
        }
        catch (Exception e)
        {

        }
    }
    public void drawCirclePath()
    {
        try{
            if(circle_selected)
            {
                undoPathAndPaint();
                constructCirclePath();
                addPathAndPaintToList();
                updateUndoRedoPathsWhenShapesEnabled();
                initiatePathAndPaint();
                invalidate();
            }
        }
        catch (Exception e)
        {

        }
    }
    public void drawTriandlePath()
    {
        try{
            if(triangle_selected)
            {
                undoPathAndPaint();
                constructTrianglePath();
                addPathAndPaintToList();
                updateUndoRedoPathsWhenShapesEnabled();
                initiatePathAndPaint();
                invalidate();
            }
        }
        catch (Exception e)
        {

        }
    }
    public void drawShapesPath()
    {
        drawLinePath();
        drawRectanglePath();
        drawCirclePath();
        drawTriandlePath();
        invalidate();
    }
    public void disableShape()
    {
        triangle_selected=false;
        line_selected=false;
        circle_selected=false;
        rect_selected=false;
        invalidate();

    }

    ////////////////////////////////////////////////////////////////////////////
    //////////////////////draw shapes ends////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////

    public void disableAll()
    {
        disableShape();
        setErase = false;
        invalidate();

    }
    public void disablePaintOption()
    {
        setErase = false;
        iscrop = false;
        invalidate();

    }

    public void initCrop()
    {
        canCrop = true;
        invalidate();
    }
    public void drawOuterLine(Canvas canvas)
    {
        try{
            selected_path_paint=new Paint();

            selected_path_paint.setStrokeWidth(20);

            selected_path_paint.setStyle(Paint.Style.FILL_AND_STROKE);
            selected_path_paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));
            if(paint_list.get(_selectedpath_index-1).getColor() == Color.BLACK)
            {
                selected_path_paint.setColor(Color.GREEN);
                canvas.drawPath(selected_path,selected_path_paint);
                invalidate();
            }
            else
            {selected_path_paint.setColor(Color.BLACK);

                canvas.drawPath(selected_path,selected_path_paint);
                invalidate();
            }
        }
        catch (Exception e)
        {

        }
        invalidate();
    }

    public void scaless(int y){}

   /* public void scale_vline(int y,int width1){
        y1=y;
        x_vline=width1/2-width1/7;
    }

    public void scale_hline(int y,int height1){
        y2=y;
        y_hline=height1/2-height1/7;
    }

    public void scale_sq(int x,int y){
        x_sq=x/2+x/4;
        y_sq=y/2+y/4;
    }

    public  void scale_tr(int x1,int y1){
        x=x1/3;
        y=y1;
        y_tri=y1-y1/3;
        width=x1-x1/3;
    }

    public void scale_pen(int x1,int y1){
        width_pen=x1-x1/4;
        height_pen=y1-y1/4;
    }
    public void scale_he(int x1,int y1){

        width_he=x1-x1/4;
        height_he=y1-y1/4;
        invalidate();
    }

    public void scale_circle(int x,int y){
        x_circle1=x/2-x/10;
        y_circle1=y/2-y/10;
        radius_circle=y_circle1/2;
    }*/

    public void scale_vline(int y,int width1){
        Log.v("width_vert",width1+"height_y"+y);
        y1=y;
        x_vline=width1/2-width1/7;
    }

    public void scale_hline(int width,int height1){
        Log.v("width_hr",width+"height"+height1);
        y2=width;
        y_hline=height1/2-height1/7;
    }

    public void scale_sq(int x,int y){
        int gx=finalreduceamt-50;
        x_sq=x-gx;
        y_sq=y-gx;
    }

    public void scale_tr(int x1,int y1){
        x=(x1/3)+5;
        y=y1;
        y_tri=y1-finalreduceamt;
        width=x1-finalreduceamt;
        retrieveCount=2;


        Log.v("ytriwit", String.valueOf(y_tri));
        invalidate();
    }

    public void scale_pen(int x1,int y1){
        width_pen=x1-finalreduceamt;
        height_pen=y1-finalreduceamt;
        retrieveCount=2;
    }
    public void scale_he(int x1,int y1){

        width_he=x1-finalreduceamt;
        height_he=y1-finalreduceamt;
/*        width_he=x1-x1/4;
        height_he=y1-y1/4;*/

        retrieveCount=2;
        invalidate();
    }

    public void scale_circle(int x,int y){
        x_circle1=(x-finalreduceamt)/2;
        y_circle1=(y-finalreduceamt)/2;
        radius_circle=(y-finalreduceamt)/2;
        retrieveCount=2;
    }

    public void scale_tr1(int x1)
    {
        x_tri=x1;
    }

    public void setVertical(boolean v){
        v_line=v;
    }

    public void setHorizontal(boolean v){
        h_line=v;
    }

    public void setSquare(boolean v){
        s_line=v;
    }

    public void setTriangle(boolean v){
        t_line=v;
    }
    public void setHexagon(boolean v){
        he_line=v;
    }

    public void setPentagon(boolean v){ pen_line=v; }

    public void setCircle(boolean v){ circle_line=v; }

    public void undoPathAndPaint()
    {
        try{
            if (path_list.size() > 0)
            {
                undonePaths.add(path_list.remove(path_list.size() - 1));
                mUndonePaints.add(paint_list.remove(paint_list.size() - 1));
                invalidate();
            }
        }
        catch (Exception e)
        {

        }
    }
    public void updateUndoRedoPathsForNormalPaintOption()
    {
        try{
            completePath_undo.add(current_path);
            normalPath_undo.add(current_path);
            undo_paints.add(current_paint);
            pathsRemovedOrAdded_by_undo.clear();
            normalPath_redo.clear();
            selectBtn_deletedPaths_redo.clear();
            drawShapeEnabledPaths_redo.clear();
            redo_paints.clear();
            if(!ispath_got)
            {
                wassecondbitmap_deleted=true;
                issecondbitmap_invisible=false;
            }
        }
        catch (Exception e)
        {

        }
    }
    public void updateUndoRedoPathsWhenShapesEnabled()
    {
        try{
            completePath_undo.add(current_path);
            drawShapeEnabledPaths_undo.add(current_path);
            undo_paints.add(current_paint);
            pathsRemovedOrAdded_by_undo.clear();
            normalPath_redo.clear();
            selectBtn_deletedPaths_redo.clear();
            drawShapeEnabledPaths_redo.clear();
            redo_paints.clear();
        }
        catch (Exception e)
        {

        }

    }

    public void undoPaths()
    {
        if(path_list.size()>0 && paint_list.size()>0) {
            redoPathList.add(path_list.remove(path_list.size() - 1));
            redoPaintList.add(paint_list.remove(paint_list.size() - 1));
            invalidate();
            if (path_list.size() > 0) {
                PaintModule.setUndoActive();
            } else {
                PaintModule.setUndoPassive();
            }
            if (redoPaintList.size() > 0) {
                PaintModule.setRedoActive();
            }
        }

    }
    ////////////
    public void redoPaths()
    {
        if(redoPathList.size()>0 && redoPaintList.size()>0) {
            path_list.add(redoPathList.remove(redoPathList.size() - 1));
            paint_list.add(redoPaintList.remove(redoPaintList.size() - 1));
            invalidate();

            if (path_list.size() > 0) {
                PaintModule.setUndoActive();
            }
            if (redoPaintList.size() < 1) {
                PaintModule.setRedoPassive();
            } else {
                PaintModule.setRedoActive();
            }
        }
    }
    public void undoPaths1()
    {
        try{
            isUndo=true;
            if(completePath_undo.size() > 0) {

                if( selectBtn_deletedPaths_undo.size()>0 && isUndo) {
                    if (completePath_undo.get(completePath_undo.size() - 1) ==
                            selectBtn_deletedPaths_undo.get(selectBtn_deletedPaths_undo.size() - 1))
                    {
                        System.out.println("current_path in delete");


                        pathsRemovedOrAdded_by_undo.add(completePath_undo.get(completePath_undo.size() - 1));
                        selectBtn_deletedPaths_redo.add(completePath_undo.get(completePath_undo.size() - 1));

                        path_list.add(completePath_undo.get(completePath_undo.size() - 1));
                        paint_list.add(undo_paints.get(undo_paints.size() - 1));

                        redo_paints.add(undo_paints.get(undo_paints.size() - 1));
                        undo_paints.remove(undo_paints.size() - 1);

                        selectBtn_deletedPaths_undo.remove(selectBtn_deletedPaths_undo.size() - 1);

                        completePath_undo.remove(completePath_undo.size() - 1);
                        isUndo=false;

                    }

                }

                if(normalPath_undo.size()>0 && isUndo)
                {
                    if (completePath_undo.get(completePath_undo.size() - 1) ==
                            normalPath_undo.get(normalPath_undo.size() - 1))
                    {
                        System.out.println("current_path in normal");

                        pathsRemovedOrAdded_by_undo.add(completePath_undo.get(completePath_undo.size() - 1));
                        normalPath_redo.add(completePath_undo.get(completePath_undo.size() - 1));

                        path_list.remove(completePath_undo.get(completePath_undo.size()-1));
                        paint_list.remove(undo_paints.get(undo_paints.size()-1));

                        //  normalPath_redo.add(normalPath_undo.get(normalPath_undo.size()-1));
                        normalPath_undo.remove(normalPath_undo.size() - 1);

                        redo_paints.add(undo_paints.get(undo_paints.size() - 1));
                        undo_paints.remove((undo_paints.size() - 1));

                        completePath_undo.remove(completePath_undo.size() - 1);

                        isUndo = false;

                    }

                }

                if(drawShapeEnabledPaths_undo.size()>0 && isUndo) {
                    if (completePath_undo.get(completePath_undo.size() - 1) ==
                            drawShapeEnabledPaths_undo.get(drawShapeEnabledPaths_undo.size() - 1))
                    {
                        System.out.println("current_path in shape");
                        pathsRemovedOrAdded_by_undo.add(completePath_undo.get(completePath_undo.size() - 1));
                        drawShapeEnabledPaths_redo.add(completePath_undo.get(completePath_undo.size() - 1));

                        path_list.remove(completePath_undo.get(completePath_undo.size()-1));
                        paint_list.remove(undo_paints.get(undo_paints.size()-1));

                        redo_paints.add(undo_paints.get(undo_paints.size() - 1));
                        undo_paints.remove(undo_paints.size() - 1);

                        completePath_undo.remove(completePath_undo.size() - 1);

                        drawShapeEnabledPaths_undo.remove(drawShapeEnabledPaths_undo.size() - 1);

                        path_list.add(completePath_undo.get(completePath_undo.size() - 1));
                        paint_list.add(undo_paints.get(undo_paints.size() - 1));

                        pathsRemovedOrAdded_by_undo.add(completePath_undo.get(completePath_undo.size() - 1));
                        drawShapeEnabledPaths_redo.add(completePath_undo.get(completePath_undo.size() - 1));

                        redo_paints.add(undo_paints.get(undo_paints.size() - 1));
                        // drawShapeEnabledPaths_redo.add(completePath_undo.get(completePath_undo.size() - 1));

                        //  pathsRemovedOrAdded_by_undo.add(completePath_undo.get(completePath_undo.size() - 1));

                        isUndo=false;
                        //   undo_paints.remove(undo_paints.size() - 1);




                    }
                }
            }

            if(path_list.size()>0)
            {
                PaintModule.setUndoActive();
            }
            else
            {
                PaintModule.setUndoPassive();
            }
            if(redo_paints.size()>0)
            {
                PaintModule.setRedoActive();
            }
        }
        catch (Exception e)
        {

        }
      /*  else if(completePath_undo.size()<=0 && !wassecondbitmap_deleted)
        {
           *//*
            isBitmapUndo=true;*//*

            second_bitmap.recycle();
            bitmap.recycle();
            ispath_got=false;
            issecondbitmap_invisible=true;
            System.out.println("comple undon current_path ==0");
        }*/
        invalidate();
    }

    public void redoPaths1() {
        isRedo=true;

      /*  if(issecondbitmap_invisible)
        {
            ispath_got=true;
            issecondbitmap_invisible=false;
        }
        else*/
        try{
            if (pathsRemovedOrAdded_by_undo.size() > 0) {
                if (normalPath_redo.size() > 0 && isRedo) {
                    if (pathsRemovedOrAdded_by_undo.get(pathsRemovedOrAdded_by_undo.size() - 1) ==
                            normalPath_redo.get(normalPath_redo.size() - 1)) {
                        System.out.println("current_path in normal redo");

                        path_list.add(normalPath_redo.get(normalPath_redo.size() - 1));
                        paint_list.add(redo_paints.get(redo_paints.size() - 1));
                        completePath_undo.add(normalPath_redo.get(normalPath_redo.size() - 1));
                        normalPath_undo.add(normalPath_redo.get(normalPath_redo.size() - 1));
                        undo_paints.add(redo_paints.get(redo_paints.size() - 1));

                        normalPath_redo.remove(normalPath_redo.size() - 1);
                        redo_paints.remove(redo_paints.size() - 1);

                        pathsRemovedOrAdded_by_undo.remove(pathsRemovedOrAdded_by_undo.size() - 1);
                        isRedo=false;
                    }
                }
                if (selectBtn_deletedPaths_redo.size() > 0 && isRedo) {
                    if (pathsRemovedOrAdded_by_undo.get(pathsRemovedOrAdded_by_undo.size() - 1) ==
                            selectBtn_deletedPaths_redo.get(selectBtn_deletedPaths_redo.size() - 1)) {
                        System.out.println("current_path in delete redo");

                        path_list.remove(selectBtn_deletedPaths_redo.get(selectBtn_deletedPaths_redo.size() - 1));
                        paint_list.remove(redo_paints.get(redo_paints.size() - 1));
                        completePath_undo.add(selectBtn_deletedPaths_redo.get(selectBtn_deletedPaths_redo.size() - 1));
                        normalPath_undo.add(selectBtn_deletedPaths_redo.get(selectBtn_deletedPaths_redo.size() - 1));
                        undo_paints.add(redo_paints.get(redo_paints.size() - 1));

                        selectBtn_deletedPaths_redo.remove(selectBtn_deletedPaths_redo.size() - 1);
                        redo_paints.remove(redo_paints.size() - 1);

                        pathsRemovedOrAdded_by_undo.remove(pathsRemovedOrAdded_by_undo.size() - 1);
                        isRedo=false;

                    }
                }
                if (drawShapeEnabledPaths_redo.size() > 0 && isRedo) {
                    if (pathsRemovedOrAdded_by_undo.get(pathsRemovedOrAdded_by_undo.size() - 1) ==
                            drawShapeEnabledPaths_redo.get(drawShapeEnabledPaths_redo.size() - 1)) {
                        System.out.println("current_path in shape redo");

                        path_list.remove(drawShapeEnabledPaths_redo.get(drawShapeEnabledPaths_redo.size() - 1));
                        paint_list.remove(redo_paints.get(redo_paints.size() - 1));
                        // normalPath_undo.add(drawShapeEnabledPaths_redo.get(drawShapeEnabledPaths_redo.size()-1));
                        // undo_paints.add(redo_paints.get(redo_paints.size()-1));
                        drawShapeEnabledPaths_redo.remove(drawShapeEnabledPaths_redo.size() - 1);
                        redo_paints.remove(redo_paints.size() - 1);
                        pathsRemovedOrAdded_by_undo.remove(pathsRemovedOrAdded_by_undo.size() - 1);
                        path_list.add(drawShapeEnabledPaths_redo.get(drawShapeEnabledPaths_redo.size() - 1));
                        paint_list.add(redo_paints.get(redo_paints.size() - 1));
                        completePath_undo.add(drawShapeEnabledPaths_redo.get(drawShapeEnabledPaths_redo.size() - 1));
                        drawShapeEnabledPaths_undo.add(drawShapeEnabledPaths_redo.get(drawShapeEnabledPaths_redo.size() - 1));
                        undo_paints.add(redo_paints.get(redo_paints.size() - 1));

                        drawShapeEnabledPaths_redo.remove(drawShapeEnabledPaths_redo.size() - 1);
                        redo_paints.remove(redo_paints.size() - 1);

                        pathsRemovedOrAdded_by_undo.remove(pathsRemovedOrAdded_by_undo.size() - 1);
                        isRedo=false;

                    }

                }
            }


            if(path_list.size()>0)
            {
                PaintModule.setUndoActive();
            }
            if(redo_paints.size()<1)
            {
                PaintModule.setRedoPassive();
            }
            else
            {
                PaintModule.setRedoActive();

            }
        }
        catch (Exception e)
        {

        }
        invalidate();
    }


    public void getpaths(String path_list){
        try{
            path_got=path_list;
            ispath_got=true;

/*
            if (ispath_got == true) {
                ispath_got=false;
                Log.v("is_path_got_is_","true");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap  second_bitmap1 = BitmapFactory.decodeFile(path_got, options);
           */
/* Rect rectangle = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            canvas.drawBitmap(second_bitmap, null, rectangle, null);*//*

                converBit(second_bitmap1);
                merging(bitmap,second_bitmap1,bitmap.getWidth(),bitmap.getHeight());

                invalidate();
            }
*/
        }
        catch (Exception e)
        {

        }
    }
    public void getpathnull(){
        ispath_got=false;
    }
    public void checkPageHasPaint()
    {
        try{
            if(path_list.size()>0  || ispath_got || was_cropped)
            {
                isPainted=true;
            }
        }
        catch (Exception e)
        {

        }
    }
    public void drawMarker()
    {
        ispen=false;
        ismarker=true;
        current_paint.setAlpha(90);
        invalidate();
    }
    public void drawPen()
    {
        ispen=true;
        ismarker=false;
        current_paint.setAlpha(255);
        invalidate();

    }

    public int screendimension()
    {
        int screenHeight=0;
        try{
            DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(metrics);
       /* DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);*/
            screenHeight = metrics.heightPixels;
            int screenWidth = metrics.widthPixels;
            Log.i("MY", "Actual Screen Height = " + screenHeight + " Width = " + screenWidth);
        }
        catch (Exception e)
        {

        }
        return screenHeight;
    }


    private double getScreenDimension(){
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double)width / (double)dens;
        double hi = (double)height / (double)dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x+y);

        String[] screenInformation = new String[3];
        screenInformation[0] = String.valueOf(width) + " px";
        screenInformation[1] = String.valueOf(height) + " px" ;
        screenInformation[2] = String.format("%.2f", screenInches) + " inches" ;
        DecimalFormat formater = new DecimalFormat("#.00");
        double inch_val=screenInches;

        Log.v("inchesin samsung", formater.format(inch_val));

        return Double.parseDouble(formater.format(inch_val));
    }

    /////////done by ezhil///////////////

    public void changeCrop(){


        bitmap=getBitmap();
        flgPathDraw1=true;
        cropClick=true;
        path_list.clear();
        paint_list.clear();
        initPaint();
        invalidate();

    }

    public void changePaint(){
        flgPathDraw1=false;
        mfirstpoint=null;
        bfirstpoint1=false;
        cropClick=false;
        ptClick=false;
        resultingval=false;
        points.clear();
        initPaint();
        invalidate();
    }


    private boolean comparepoint(Point first, Point current) {
        int left_range_x = (int) (current.x - 3);
        int left_range_y = (int) (current.y - 3);

        int right_range_x = (int) (current.x + 3);
        int right_range_y = (int) (current.y + 3);

        if ((left_range_x < first.x && first.x < right_range_x)
                && (left_range_y < first.y && first.y < right_range_y)) {
            if (points.size() < 10) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }


    private void showcropdialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override            public void onClick(DialogInterface dialog, int which) {
                Intent intent;
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        Log.v("cropButtonare","hereclicked");

                        PaintModule.setUndoPassive();
                        points_replace.addAll(points);
                        points.clear();
                        src_out=true;
                        callbitmap=false;
                        check_crop_val=true;
                        ctrl_crop=false;
                        invalidate();
                       /* points_replace.addAll(points);
                        points.clear();*/
                        /*callbitmap=true;*/


                       /* screenshot=true;
                        points_replace.addAll(points);
                        points.clear();
                        cropintVal=1;
                        invalidate();*/

                       /* intent = new Intent(mContext, ImageCropActivity.class);
                        intent.putExtra("crop", true);
                        mContext.startActivity(intent);*/
                       /* screenshot=true;
                        points_replace.addAll(points);
                        points.clear();
                        invalidate();*/



                       /* captureCanvasScreen();
                        secbitmap=bitmapFunction();
                        ptclick=true;
                        points.clear();
                        invalidate();*/


                       /* ptclick=true;
                        Log.v("secbitmap", String.valueOf(secbitmap));
                        invalidate();*/
                      /* String pathing="/storage/emulated/0/pippo3.png";
                        intent = new Intent(mContext, MainDrawActivity.class);
                        intent.putExtra("crop", pathing);
                        intent.putExtra("width",Collections.max(pointx));
                        intent.putExtra("height",Collections.max(pointy));
                        mContext.startActivity(intent);*/
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                      /*  points_replace.addAll(points);
                        points.clear();
                        src_out=true;
                        callbitmap=false;
                        invalidate();*/
                       /* points_replace.addAll(points);
                        points.clear();
                        src_out=true;
                        callbitmap=false;
                        invalidate();*/
                        PaintModule.setUndoPassive();
                        points_draw=false;
                        points_replace.addAll(points);
                        points.clear();
                        callbitmap=true;
                        check_crop_val=true;
                        ctrl_crop=false;
                        invalidate();

                      /*  screenshot=false;
                        screenshot1=true;
                        points_replace.addAll(points);
                        points.clear();
                        cropintVal=2;
                        invalidate();*/
                       /* intent = new Intent(mContext, ImageCropActivity.class);
                        intent.putExtra("crop", false);
                        mContext.startActivity(intent);*/
                        bfirstpoint1 = false;
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Do you Want to save Crop or Non-crop image?")
                .setPositiveButton("Cut", dialogClickListener)
                .setNegativeButton("Copy", dialogClickListener).show()
                .setCancelable(true);
    }

    public Bitmap bitmapFunction(int tt){

        points.addAll(points_replace);
        points_replace.clear();
        boolean crop=true;
        int xx= Collections.max(pointx1);
        int yy=Collections.max(pointy1);
        bt_endx= Collections.max(pointx1);
        bt_endy=Collections.max(pointy1);
        bt_startX=Collections.min(pointx1);
        bt_startY=Collections.min(pointy1);
        bt_startX=(bt_endx+bt_startX)/2;
        bt_startY=(bt_endy+bt_startY)/2;
        Log.v("maximum_xx_ee",bt_startX+"maximumY"+bt_startY+"mini"+Collections.min(pointx1)+"y"+Collections.min(pointy1)+"max"+Collections.max(pointx1)+"y "+Collections.max(pointy1));
        Log.v("divide_val", String.valueOf(bt_endx+bt_startX/2));
        pointx1.clear();
        pointy1.clear();
        Bitmap bitmap2 = null;

        /*Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.happy);
*/

        /*if(path_canvas.size()!=0) {

            String hPath = path_canvas.get(path_canvas.size() - 1);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap2 = BitmapFactory.decodeFile(hPath, options);
            Log.v("within_bitmap_noncrop",hPath);
        }
        else{
            bitmap2 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.happy);
        }*/

        bitmap2=bitmap;
        Bitmap resultingImage = Bitmap.createBitmap(xx,
                yy, bitmap2.getConfig());


       /* Bitmap.createBitmap(getContext(),60,60,xx,yy,bitmap2.getConfig());*/
        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
            Log.v("x&yPointbitmap",points.get(i).x+" ypointview"+points.get(i).y);
        }
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Log.v("src_out_mode","are_in_side");
        } else {
            Log.v("src_out_mode","are in outt");
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        // converBit(resultingImage);
        checkkk(resultingImage);


        // merge(bitmap,resultingImage);




        // converBit(resultingImage);

       /* Bitmap bitmap = Bitmap.createBitmap(imgView.getWidth(),imgView.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        imgView.draw(canvas);*/
      /*  Bitmap result =Bitmap.createBitmap(resultingImage,Collections.min(pointx), Collections.min(pointy), xx, yy);
        bitmap.recycle();
        Canvas canvas1 = new Canvas(result);
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);

        Path path1 = new Path();
        for (int i = 0; i < points.size(); i++) {
            path1.lineTo(points.get(i).x, points.get(i).y);
            Log.v("x&yPointbitmap",points.get(i).x+" ypointview"+points.get(i).y);
        }
        canvas1.drawPath(path1, paint1);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }
        canvas1.drawBitmap(bitmap2, 0, 0, paint);

        String pathing="/storage/emulated/0/pippo3.png";
       *//* clipArt.clipart_set_img(pathing);*//*
        converBit1(result);*/
        return resultingImage;
    }

    public void checkkk(Bitmap bt){
        //resultingval=true;
        res_bitmap=bt;
        resultingval=true;
        Log.v("value_bitmappp","isres_bitmap");
        invalidate();


        //bitmap_img_array.add(new BitmapModel(res_bitmap,10,10));
    }

    public void merging(Bitmap org,Bitmap dum,int dumX,int dumY){
        Bitmap bitmap2 = null;
        Bitmap resultingImage;
        resultingImage = Bitmap.createBitmap(org.getWidth(), org.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultingImage);
        canvas.drawBitmap(org, 0f, 0f, null);
        canvas.drawBitmap(dum, dumX, dumY, null);
        bitmap=resultingImage;
        // bitmap_array.add(bitmap);
        Log.v("creation_bt","arebit");
        flgPathDraw1=true;
        bfirstpoint1=false;
        mfirstpoint=null;
        points_draw=true;
        points.clear();
        points_replace.clear();
        pointx.clear();
        pointy.clear();
        btX=10;btY=10;
        path_list.clear();
        paint_list.clear();
        check_crop_val=false;
        initPaint();
        invalidate();
    }
    public void mergingNoCrop(Bitmap org){
        Bitmap bitmap2 = null;
        Bitmap resultingImage;
        resultingImage = Bitmap.createBitmap(org.getWidth(), org.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultingImage);
        canvas.drawBitmap(org, 0f, 0f, null);
        bitmap=resultingImage;
        // bitmap_array.add(bitmap);
        Log.v("creation_bt","arebit");
        flgPathDraw1=true;
        bfirstpoint1=false;
        mfirstpoint=null;
        points_draw=true;
        points.clear();
        points_replace.clear();
        pointx.clear();
        pointy.clear();
        btX=10;btY=10;
        path_list.clear();
        paint_list.clear();
        check_crop_val=false;
        initPaint();
        invalidate();
    }

    public Bitmap bitmapFunctionSrc(int tt){

        points.addAll(points_replace);
        points_replace.clear();

        boolean crop=false;
        int xx= Collections.max(pointx1);
        int yy=Collections.max(pointy1);
        Log.v("maximum_xx",xx+"maximumY"+yy);
        Bitmap bitmap2 = null;

        /*Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
                R.drawable.happy);
*/

        /*if(path_canvas.size()!=0) {

            String hPath = path_canvas.get(path_canvas.size() - 1);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap2 = BitmapFactory.decodeFile(hPath, options);
            Log.v("within_bitmap_noncrop",hPath);
        }
        else{
            bitmap2 = BitmapFactory.decodeResource(getResources(),
                    R.drawable.happy);
        }*/

        bitmap2=bitmap;
        Bitmap resultingImage = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), bitmap2.getConfig());

       /* Bitmap.createBitmap(getContext(),60,60,xx,yy,bitmap2.getConfig());*/
        Canvas canvas = new Canvas(resultingImage);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Path path = new Path();
        for (int i = 0; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
            Log.v("x&yPointbitmap",points.get(i).x+" ypointview"+points.get(i).y);
        }
        canvas.drawPath(path, paint);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            Log.v("src_out_mode","are_in_side");
        } else {
            Log.v("src_out_mode","are in outt");
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        canvas.drawBitmap(bitmap2, 0, 0, paint);

        // converBit(resultingImage);
        partial_bitmap=resultingImage;
        SaveImage(partial_bitmap,"saving_first");


        // merge(bitmap,resultingImage);




        // converBit(resultingImage);

       /* Bitmap bitmap = Bitmap.createBitmap(imgView.getWidth(),imgView.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        imgView.draw(canvas);*/
      /*  Bitmap result =Bitmap.createBitmap(resultingImage,Collections.min(pointx), Collections.min(pointy), xx, yy);
        bitmap.recycle();
        Canvas canvas1 = new Canvas(result);
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);

        Path path1 = new Path();
        for (int i = 0; i < points.size(); i++) {
            path1.lineTo(points.get(i).x, points.get(i).y);
            Log.v("x&yPointbitmap",points.get(i).x+" ypointview"+points.get(i).y);
        }
        canvas1.drawPath(path1, paint1);
        if (crop) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }
        canvas1.drawBitmap(bitmap2, 0, 0, paint);

        String pathing="/storage/emulated/0/pippo3.png";
       *//* clipArt.clipart_set_img(pathing);*//*
        converBit1(result);*/
        return resultingImage;
    }

/*
    public void changeNmae(Bitmap bit){
        String filename = "pippo.png";
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, "EvernoteClone/Pictures");
        String file_path=dest+"/"+"paint_"+System.currentTimeMillis()+".png";
        //Log.v("full_file_nameis",file_path.getPath());

        Bitmap bitmap = bit;
        try {
            FileOutputStream out = new FileOutputStream(file_path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.v("full_file_nameis",file_path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

public void clearCropPoint(){



    if(check_crop_val==true){
        ptClick=false;
        resultingval=false;
        current_path=new Path();
        initPaint();
        merging(bitmap,res_bitmap,btX,btY);
    }

/*
    if(res_bitmap!=null){
        current_path=new Path();
        Log.v("paint_are_nt","herecleared");
        path_list.clear();
        paint_list.clear();
    }
*/
}

    public void clearCropPointcheck(){




            ptClick=false;
            resultingval=false;
            current_path=new Path();
            initPaint();
            mergingNoCrop(bitmap);


/*
    if(res_bitmap!=null){
        current_path=new Path();
        Log.v("paint_are_nt","herecleared");
        path_list.clear();
        paint_list.clear();
    }
*/
    }


public void clearAllpt(){
    path_list.clear();
    paint_list.clear();
    PaintModule.setUndoPassive();
    points_replace.addAll(points);
    points.clear();
    invalidate();
}

    public void clear()
    {
        path_list.clear();
        paint_list.clear();
        undonePaths.clear();
        mUndonePaints.clear();
        mcanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void resetPath(Path path,Canvas can){


        path=new Path();
        path.reset();

    }

    private static void SaveImage(Bitmap finalBitmap,String name) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = name+".png";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            Log.v("img_File", String.valueOf(file));
            FileOutputStream out = new FileOutputStream(file);
            Log.v("img_File", String.valueOf(file));
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            Log.v("img_File", String.valueOf(file));
            out.flush();
            out.close();
            Log.v("img_File", String.valueOf(file));

        } catch (Exception e) {
            e.printStackTrace();
            Log.v("img_File_exce", String.valueOf(e));
        }
    }


}

