package com.seu;

import com.seu.config.SecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;




@SpringBootTest
public class CourseSelectionSystemApplicationTests {

    /*@Test
    void contextLoads() {
    }*/
    /**
     * 测试JWT令牌生成
     */
    @Test
    public void testGenJwt(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 10086);
        claims.put("name", "张三");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "SEUxuanke")//签名算法
                .setClaims(claims)//自定义内容(载荷)
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))//设置有效期为1h
                .compact();
        System.out.println(jwt);
    }

    /**
     * 测试JWT令牌解析
     */
    @Test
    public void testParseJwt(){
        Claims claims = Jwts.parser()
                .setSigningKey("SEUxuanke")
                .build()
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5byg5LiJIiwiaWQiOjEwMDg2LCJleHAiOjE3MTU1MTQxMTN9.ZsZA7TFYWV3FlenqKAs0GxKrAaLJBLk5gJEEFUPoCpU")
                .getBody();
        System.out.println(claims);
    }

    /**
     * 测试密码加密
     */
    @Test
    public void testEncodePassword(){
        SecurityConfig securityConfig = new SecurityConfig();
        String encodedPassword = securityConfig.encodePassword("123456");
        System.out.println(encodedPassword);

        boolean mark = securityConfig.matchPassword("123456", encodedPassword);
        System.out.println(mark);
    }

    /**
     * 测试密码校验
     */
    @Test
    public void testMatchPassword(){

    }

    /**
     * 根据time值计算出上课时间
     */
    @Test
    public void testGetFriendlyTime(){
        for(int time = 0; time < 168; time++){
            int week = time / 8;
            int Num = time % 8;
            int danshuang = week / 7;
            int day = (week + 7) % 7;

            String danshuangString = new String();
            if(danshuang == 0) danshuangString = "单周";
            else if(danshuang == 1) danshuangString = "双周";
            else danshuangString = "单双周";

            String jie = new String();
            switch(Num){
                case 0: jie = "1-2"; break;
                case 1: jie = "3-4"; break;
                case 2: jie = "3-5";break;
                case 3: jie = "6-7";break;
                case 4: jie = "8-9";break;
                case 5: jie = "8-10";break;
                case 6: jie = "11-12";break;
                case 7: jie = "11-13";break;
                default: jie = "节数错误";
            }
            System.out.print(time + "对应: ");
            System.out.println(danshuangString + "周" + (day + 1) + "第" + jie + "节");
        }

    }
}
