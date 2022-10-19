package org.circuitrunners.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Mecanum {
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor bl;
    private DcMotor br;
    private BNO055IMU imu;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    public Mecanum(DcMotor m1, DcMotor m2, DcMotor m3, DcMotor m4, BNO055IMU imu1) {
        fl = m1;
        fr = m2;
        bl = m3;
        br = m4;
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        imu = imu1;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
    }

    public boolean drive(double y, double x, double rx, boolean p, boolean c) {
        double angle = -imu.getAngularOrientation().firstAngle;

        //Recalibrate imu angle
        if (!p && c) {
            angle = 0;
            return true;
        }

        //Mecanum math
        double rotX = x * Math.cos(angle) - y * Math.sin(angle);
        double rotY = x * Math.sin(angle) + y * Math.cos(angle);

        double d = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double flpwr = (rotY + rotX + rx) / d;
        double blpwr = (rotY - rotX + rx) / d;
        double frpwr = (rotY - rotX - rx) / d;
        double brpwr = (rotY + rotX - rx) / d;

        fl.setPower(flpwr);
        bl.setPower(blpwr);
        fr.setPower(frpwr);
        br.setPower(brpwr);
        return false; //Angle not recalibrated
    }

}
