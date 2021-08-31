/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import cezeri.factory.FactoryUtils;
import cezeri.matrix.CMatrix;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.NamedCollectionConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cezerilab
 */
public class TestXStream {

    public static void main(String[] args) {
        XStream xs = new XStream(new DomDriver());
        xs.alias("annotation", Annotation.class);
        xs.alias("source", Source.class);
        xs.alias("size", Size.class);
        xs.alias("object", _Object.class);
        xs.alias("bndbox", BoundingBox.class);
        //xs.addImplicitCollection(Annotation.class, "object");
        xs.addImplicitArray(Annotation.class, "object");
//        final NamedCollectionConverter namesConverter = new NamedCollectionConverter(
//                xs.getMapper(), "object", _Object.class);
//        xs.registerLocalConverter(Annotation.class, "object", namesConverter);

//        Annotation an = new Annotation();
//        an.object=new _Object[]{new _Object(),new _Object()};
//        String s = xs.toXML(an);
//        System.out.println(s);
//        Annotation an2=(Annotation) xs.fromXML(s);
        
        String path = "C:\\Users\\cezerilab\\Downloads\\yeni_traffic_signs_dataset_v1\\traffic_signs_dataset_v1\\train";
        File[] files = FactoryUtils.getFileListInFolder(path);

        Annotation a;
        CMatrix cm = CMatrix.getInstance();
        System.out.println("toplam:"+files.length);
        for (int i = 1705; i < files.length; i++) {
            
            if (files[i].getAbsolutePath().contains(".xml")) {
                System.out.println(i+".file:"+files[i].getAbsolutePath());
                String xml = FactoryUtils.readFile(files[i].getAbsolutePath());
                xml = xml.replace("class", "class_type");
//                System.out.println(xml);
                a = (Annotation) xs.fromXML(xml);
                
                for (_Object obj : a.object) {
                    obj.name=obj.name.replace("/", "");
                    FactoryUtils.makeDirectory(path + "/" + obj.name);
//                    FactoryUtils.copyFile(new File(path + "/" + a.file), new File(path + "/" + obj.name + "/" + a.file));
                    int from_nr=obj.bndbox.ymin;
                    int from_nc=obj.bndbox.xmin;
                    int to_nr=(obj.bndbox.ymax>=1440)?1439:obj.bndbox.ymax;
                    int to_nc=(obj.bndbox.xmax>=1080)?1079:obj.bndbox.xmax;
                    
                    String sf=new File(path + "/" + a.file).getAbsolutePath();
                    cm=cm
                            .imread(sf)
                            .cmd(from_nr+":"+to_nr, from_nc+":"+to_nc)
                            .imresize(224, 224)
//                            .imshow()
                            .imsave(new File(path + "/" + obj.name + "/" + a.file).getParent()+"/"+System.nanoTime()+".jpg")
                            ;
//                    System.out.println("");
                }
            }
        }

    }
}

class Source {
    String database = "Unknown";
}

class Size {
    int width = 1440;
    int height = 1080;
    int depth = 3;
}

class BoundingBox {
    int xmin = 932;
    int ymin = 473;
    int xmax = 970;
    int ymax = 508;
}

class _Object {
    String name="Sagdan Gidiniz (Keep right)";
    int class_type=19;
    int pose=0;
    int truncated=0;
    int difficult=0;
    BoundingBox bndbox=new BoundingBox();
}

class Annotation {
    String folder = "traffic_sign_data";
    String file = "IMG_0684.jpg";
    String path = "traffic_sign_data/";
    Source source = new Source();
    Size size = new Size();
    int segmented;
//    @XStreamImplicit
//    List<_Object> object=new ArrayList<_Object>();
    _Object[] object;   
}
