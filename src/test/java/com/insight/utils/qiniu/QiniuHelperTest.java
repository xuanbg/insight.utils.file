package com.insight.utils.qiniu;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;

public class QiniuHelperTest extends TestCase {


    @Test
    public void testQiniuHelper() throws IOException {
        String fn = "00284fba1550409698d80a6c4ea1d65c.png";
//        String fn = "guonei.xlsx";

        byte[] bytes = QiniuHelper.read(fn);
//        File f = new File("xxx-" + fn);
//        FileOutputStream fop = new FileOutputStream(f);
//        fop.write(bytes);
//        fop.flush();
//        fop.close();
    }

}
