/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.shef.attachment.utils;

import com.primesense.nite.Quaternion;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

/**
 *
 * @author zeno
 */
public class VectorCalc {
    public Point3f convertPoint(com.primesense.nite.Point3D<Float> p) {
        Point3f point = new Point3f();
        point.x = p.getX();
        point.y = p.getY();
        point.z = p.getZ();
        return point;
    }

    public float planeAngle(Point3f a, Point3f b, Point3f c, Point3f d, Point3f e) {


        Vector3f vec1 = new Vector3f();
        vec1.sub(b, a);

        Vector3f vec2 = new Vector3f();
        vec2.sub(c, a);

        Vector3f vec3 = new Vector3f();
        vec3.sub(e, d);

        Vector3f normal = new Vector3f();
        normal.cross(vec1, vec2);

        float angle = vec3.angle(normal);

        return angle;

    }

    public float vectorAngle(Point3f a, Point3f b, Point3f c) {
        Vector3f vec1 = new Vector3f();
        vec1.sub(b, a);

        Vector3f vec2 = new Vector3f();
        vec2.sub(c, a);

        float angle = vec1.angle(vec2);
        return angle;

    }

    public float quatToAngle(Quaternion q) {
        Quat4f qv = new Quat4f();
        qv.x = q.getX();
        qv.y = q.getY();
        qv.w = q.getW();
        qv.z = q.getZ();
        AxisAngle4f a = new AxisAngle4f();
        a.set(qv);


        return a.angle;
    }
}
