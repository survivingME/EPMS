import com.qyn.project.entity.Relation;
import com.qyn.project.service.ReaderService;
import com.qyn.project.service.RelationService;
import com.qyn.project.service.impl.ReaderServiceImpl;
import com.qyn.project.service.impl.RelationServiceImpl;
import com.qyn.project.util.Code;
import com.qyn.project.util.PageBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("spring/spring-context.xml")
public class RelationTest {

    @Autowired
    private RelationService relationService;

    @Test
    public void testAddRelation() {
        Relation relation = new Relation("01234569", "美团公司", Code.RELATION_TYPE_COMPANY);
        relationService.addRelation(relation);
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("pageno", 1);
        paramMap.put("pagesize", 5);
        paramMap.put("type", Code.RELATION_TYPE_COMPANY);
        System.out.println("queryRelations");
        PageBean<Relation> pageBean = relationService.queryRelations(paramMap);
        System.out.println(pageBean);

        System.out.println("selectRelationByCode");
        Relation relation1 = relationService.selectRelationByCode("11111111");
        System.out.println(relation1);

        System.out.println("selectRelationByName");
        Relation relation2 = relationService.selectRelationByName("腾讯公司");
        System.out.println(relation2);

        System.out.println("checkByCode");
        Integer integer = relationService.checkByCode("11111111");
        System.out.println(integer);

        System.out.println("checkByName");
        Integer integer1 = relationService.checkByName("腾讯公司");
        System.out.println(integer1);
    }
}
