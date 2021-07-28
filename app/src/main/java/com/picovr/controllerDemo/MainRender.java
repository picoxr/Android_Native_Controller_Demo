package com.picovr.controllerDemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import com.picovr.client.HbController;
import com.picovr.client.HbTool;
import com.picovr.cvclient.CVController;
import com.picovr.vractivity.HmdState;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderAWD;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.ATexture.TextureException;
import org.rajawali3d.materials.textures.NormalMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.TerrainGenerator;
import org.rajawali3d.vr.renderer.VRRenderer;


import static com.picovr.cvclient.ButtonNum.*;


public class MainRender extends VRRenderer {
    private SquareTerrain terrain;
    private TextPlane plane;

    private Object3D model_cvMain, model_cvSub, model_hb;
    private Vector3 pose_cvMain, pose_cvSub, pose_hb;
    private Quaternion quat_cvMain, quat_cvSub, quat_hb;

    private CVController ctrCVMain, ctrCVSub;
    private HbController ctrHB;

    public MainRender(Context context) {
        super(context);
    }

    @Override
    public void initScene() {
        pose_cvMain = new Vector3(0, 0, 0);
        pose_cvSub = new Vector3(0, 0, 0);
        pose_hb = new Vector3(0, 0, 0);
        DirectionalLight light = new DirectionalLight(0.2f, 1f, 0f);
        light.setPower(2f);

        getCurrentScene().addLight(light);
        getCurrentScene().setBackgroundColor(0xdddddd);
        getCurrentCamera().setFarPlane(1000);

        createTerrain();
        createSpaceCruiser();
        createController();
        plane = new TextPlane(this.getCurrentScene());
    }

    private void createTerrain() {
        //
        // -- Load a bitmap that represents the terrain. Its color values will
        //    be used to generate heights.
        //

        Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.terrain);

        try {
            SquareTerrain.Parameters terrainParams = SquareTerrain.createParameters(bmp);
            // -- set terrain scale
            terrainParams.setScale(0.1f, 0.7f, 0.1f);
            // -- the number of plane subdivisions
            terrainParams.setDivisions(128);
            // -- the number of times the textures should be repeated
            terrainParams.setTextureMult(4);
            //
            // -- Terrain colors can be set by manually specifying base, middle and
            //    top colors.
            //
            // --  terrainParams.setBasecolor(Color.argb(255, 0, 0, 0));
            //     terrainParams.setMiddleColor(Color.argb(255, 200, 200, 200));
            //     terrainParams.setUpColor(Color.argb(255, 0, 30, 0));
            //
            // -- However, for this example we'll use a bitmap
            //
            terrainParams.setColorMapBitmap(bmp);
            //
            // -- create the terrain
            //
            terrain = TerrainGenerator.createSquareTerrainFromBitmap(terrainParams, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // -- The bitmap won't be used anymore, so get rid of it.
        //
        bmp.recycle();

        //
        // -- A normal map material will give the terrain a bit more detail.
        //
        Material material = new Material();
        material.enableLighting(true);
        material.useVertexColors(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        try {
            Texture groundTexture = new Texture("ground", R.drawable.ground);
            groundTexture.setInfluence(.5f);
            material.addTexture(groundTexture);
            material.addTexture(new NormalMapTexture("groundNormalMap", R.drawable.groundnor));
            material.setColorInfluence(0);
        } catch (TextureException e) {
            e.printStackTrace();
        }

        //
        // -- Blend the texture with the vertex colors
        //
        material.setColorInfluence(.5f);
        terrain.setY(-2);
        terrain.setMaterial(material);

        getCurrentScene().addChild(terrain);
    }

    private void createSpaceCruiser(){
        try {
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);

            LoaderAWD loader = new LoaderAWD(getContext().getResources(), getTextureManager(), R.raw.space_cruiser);
            loader.parse();

            Material cruiserMaterial = new Material();
            cruiserMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
            cruiserMaterial.setColorInfluence(0);
            cruiserMaterial.enableLighting(true);
            cruiserMaterial.addTexture(new Texture("spaceCruiserTex", R.drawable.space_cruiser_4_color_1));

            Object3D spaceCruiser = loader.getParsedObject();
            spaceCruiser.setMaterial(cruiserMaterial);
            spaceCruiser.setZ(-6);
            spaceCruiser.setY(1);
            getCurrentScene().addChild(spaceCruiser);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void createController(){
        try {
            LoaderOBJ model_c = new LoaderOBJ(getContext().getResources(), getTextureManager(), R.raw.hbcontroller);
            model_c.parse();

            Material cMaterial = new Material();
            cMaterial.setColorInfluence(0f);
            cMaterial.enableLighting(false);
            cMaterial.addTexture(new Texture("controllerTex", R.drawable.hbcontroller));

            model_cvMain = model_c.getParsedObject();
            model_cvMain.setScale(0.01f);
            model_cvMain.setZ(-1);
            model_cvMain.setY(-1);
            model_cvMain.setMaterial(cMaterial);

        } catch (Exception e) {
            e.printStackTrace();

        }
        model_cvSub = model_cvMain.clone(true);
        model_hb = model_cvMain.clone(true);
        getCurrentScene().addChild(model_cvMain);
        getCurrentScene().addChild(model_cvSub);
        getCurrentScene().addChild(model_hb);
    }

    @Override
    public void onNewFrame(HmdState hmdState) {
        super.onNewFrame(hmdState);

        refreshModelCVMain();
        refreshModelCVSub();
        refreshModelHB();
    }

    private void refreshModelCVMain(){

        //update main cv controller data
        if (ctrCVMain.getConnectState() != 1) {
            model_cvMain.setVisible(false);
            return;
        }else{
            model_cvMain.setVisible(true);
        }
        float[] ori1 = ctrCVMain.getOrientation();
        float[] pos1 = ctrCVMain.getPosition();
        pose_cvMain.x = pos1[0];
        pose_cvMain.y = pos1[1];
        pose_cvMain.z = pos1[2];

        quat_cvMain = new Quaternion(-ori1[3], ori1[0], ori1[1], -ori1[2]).inverse();
        model_cvMain.setPosition(pose_cvMain);
        model_cvMain.setOrientation(quat_cvMain);
    }

    private void refreshModelCVSub(){
        //update sub cv controller data
        if (ctrCVSub.getConnectState() != 1) {
            model_cvSub.setVisible(false);
            return;
        }else{
            model_cvSub.setVisible(true);
        }
        float[] ori2 = ctrCVSub.getOrientation();
        float[] pos2 = ctrCVSub.getPosition();
        pose_cvSub.x = pos2[0];
        pose_cvSub.y = pos2[1];
        pose_cvSub.z = pos2[2];
        quat_cvSub = new Quaternion(-ori2[3], ori2[0], ori2[1], -ori2[2]).inverse();

        model_cvSub.setPosition(pose_cvSub);
        model_cvSub.setOrientation(quat_cvSub);
    }

    private void refreshModelHB(){
        if (ctrHB.getConnectState() <= 0) {
            model_hb.setVisible(false);
            return;
        }else{
            model_hb.setVisible(true);
        }
        //update hummingbird controller data
        float[] pos3 = ctrHB.getPosition();
        pose_hb.x = pos3[0];
        pose_hb.y = pos3[1];
        pose_hb.z = pos3[2];
        quat_hb = new Quaternion(
                -ctrHB.getOrientation().w,
                ctrHB.getOrientation().x,
                ctrHB.getOrientation().y,
                -ctrHB.getOrientation().z
        ).inverse();
        model_hb.setPosition(pose_hb);
        model_hb.setOrientation(quat_hb);
    }

    @Override
    public void onRender(long elapsedTime, double deltaTime) {
        super.onRender(elapsedTime, deltaTime);

        StringBuilder text = new StringBuilder();

        if (ctrCVMain.getButtonState(app)
                || ctrCVSub.getButtonState(app)
                || ctrHB.getButtonState(HbTool.ButtonNum.app)) {
            text.append("app ");
        }
        if (ctrCVMain.getButtonState(click)
                || ctrCVSub.getButtonState(click)
                || ctrHB.getButtonState(HbTool.ButtonNum.click)) {
            text.append("click ");
        }
        if (ctrCVMain.getButtonState(home)
                || ctrCVSub.getButtonState(home)
                || ctrHB.getButtonState(HbTool.ButtonNum.home)) {
            text.append("home ");
        }
        if (ctrCVMain.getButtonState(volumeUp)
                || ctrCVSub.getButtonState(volumeUp)
                || ctrHB.getButtonState(HbTool.ButtonNum.volumeUp)) {
            text.append("V+ ");
        }
        if (ctrCVMain.getButtonState(volumeDown)
                || ctrCVSub.getButtonState(volumeDown)
                || ctrHB.getButtonState(HbTool.ButtonNum.volumeDown)) {
            text.append("V- ");
        }
        if (ctrCVMain.getButtonState(buttonAX) || ctrCVSub.getButtonState(buttonAX)) {
            text.append("AX ");
        }
        if (ctrCVMain.getButtonState(buttonBY) || ctrCVSub.getButtonState(buttonBY)) {
            text.append("BY ");
        }
        if (ctrCVMain.getButtonState(buttonLG)
                || ctrCVMain.getButtonState(buttonRG)
                || ctrCVSub.getButtonState(buttonLG)
                || ctrCVSub.getButtonState(buttonRG)) {
            text.append("grip ");
        }
        if (ctrCVMain.getTriggerNum() > 1
                || ctrCVSub.getTriggerNum() > 1
                || ctrHB.getTrigerKeyEvent() == 1){
            text.append("trigger ");
        }

        plane.update(text.toString());
    }

    public void setcvController(CVController ctr, CVController subCtr) {
        ctrCVMain = ctr;
        ctrCVSub = subCtr;
    }

    public void sethbController(HbController ctr){
        ctrHB = ctr;
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
