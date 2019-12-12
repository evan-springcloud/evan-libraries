package test.org.evan.libraries.utils.testdata;

import org.evan.libraries.utils.RandomDataUtil;

import java.math.BigDecimal;

public class DemoData {

    public static Demo random() {
        Demo demo = new Demo();

        demo.setFieldText(randomTitle());
        demo.setFieldNumber(new BigDecimal(RandomDataUtil.randomInt(999999)));
        demo.setFieldRadio(SexEnum.MAN.getValue() + "");
        //demo.setFieldCheckboxArray(new EnumSex[] { EnumSex.WOMAN });
        demo.setFieldSelect(SexEnum.WOMAN.getValue() + "");
        demo.setFieldHtmleditorCut(RandomDataUtil.randomName("TestCut"));
        demo.setFieldHtmleditor(RandomDataUtil.randomName("TestContent"));
        demo.setFieldProvince("330000");
        demo.setFieldCity("330100");
        demo.setFieldRegion("330106");

        return demo;
    }

    //	public static DemoEntity randomEntity() {
    //		DemoEntity demo = new DemoEntity();
    //		demo.setFieldText(randomTitle());
    //		demo.setFieldNumber(new BigDecimal(RandomData.randomInt()));
    //		demo.setFieldRadio("2");
    //		demo.setFieldCheckbox("2");
    //		demo.setFieldSelect("2");
    //		demo.setFieldHtmleditorCut(RandomData.randomName("TestCut"));
    //		demo.setFieldHtmleditor(RandomData.randomName("TestContent"));
    //		demo.setFieldProvince("330000");
    //		demo.setFieldCity("330100");
    //		demo.setFieldRegion("330106");
    //		demo.setFieldDate(new Date());
    //		demo.setFieldDatetime(new Date());
    //
    //		return demo;
    //	}

    public static String randomTitle() {
        return RandomDataUtil.randomName("DEMO");
    }

}
