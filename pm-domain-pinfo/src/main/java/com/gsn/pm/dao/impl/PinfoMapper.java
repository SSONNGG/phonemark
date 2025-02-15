package com.gsn.pm.dao.impl;


import com.gsn.pm.dao.MisBaseMapper;
import com.gsn.pm.domain.ETypeList;
import com.gsn.pm.domain.EssayList;
import com.gsn.pm.entity.Followinfo;
import com.gsn.pm.entity.Memberinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PinfoMapper extends MisBaseMapper<Memberinfo> {

    /**
     *  绑定手机号
     * @param mno
     * @param tel
     * @return
     */
    @Update("update memberinfo set tel=#{tel} where mno=#{mno}")
    int updateTel(@Param("mno") Integer mno, @Param("tel")String tel);

    /**
     * 绑定邮箱
     * @param mno
     * @param email
     * @return
     */
    @Update("update memberinfo set email=#{email} where mno=#{mno}")
    int updateEmail(@Param("mno")Integer mno,@Param("email")String email);

    /**
     * 修改密码
     * @param mno
     * @param pwd
     * @return
     */
    @Update("update memberinfo set pwd=MD5(#{pwd}) where mno=#{mno}")
    int updatePwd(@Param("mno")Integer mno,@Param("pwd")String pwd);

    /**
     * 计算用户写的文章数
     * @param mno
     * @return
     */
    @Select("select mno,COUNT(mno) messaynums FROM essayinfo  where mno=#{mno} GROUP BY mno")
    List<Memberinfo> countUserEssayNum(@Param("mno")Integer mno);

    /**
     * 粉丝数查询
     * select ifnull(count(bno),0) fnum from followinfo where `status`=1 and bno= ? GROUP BY mno
     * @param mno,bno
     * @return
     */
    @Select("select ifnull(count(bno),0) fnum from followinfo " +
            "where `status`=1 "+"and bno= #{bno} GROUP BY mno " +
            "UNION all  select ifnull(count(bno),0) fnum from followinfo " +
            "where `status`=2 and mno= #{mno} GROUP BY mno " +
            "UNION all select ifnull(count(mno),0) fnum from followinfo " +
            "where `status`=2 and bno= #{bno} GROUP BY bno")
    List<Followinfo> befollowNum(@Param("mno") Integer mno, @Param("bno") Integer bno);




    /**
     * 个人中心文章查询
     * @param eno
     * @param mno
     * @return
     */
    @Select("<script>" +
            "select b.eno,ename,epic,m.mno,mpic,nickName, " +
            "DATE_FORMAT(edate,'%m-%d') as edate,IFNULL(eheat,0) eheat,IFNULL(cnum,0) cnum from (" +
            "SELECT e.eno,ename,epic,e.mno,eheat,edate,cnum from (SELECT eno,COUNT(eno) cnum " +
            "from commentinfo GROUP BY eno) a right join essayinfo e on e.eno=a.eno) b " +
            "left join memberinfo m on m.mno=b.mno where 1=1 " +
            "<if test='eno!=null'>" +
            "and eno=#{eno}" +
            "</if>" +
            "<if test='mno!=null'>" +
            "and m.mno=#{mno}" +
            "</if>" +
            "order by edate asc" +
            "</script>")
    List<EssayList> findEssayList(@Param("eno")Integer eno,@Param("mno")Integer mno);


    /**
     * 常用专题
     * @return
     */
    @Select("<script>" +
            "select e.tno,tname from ( select mno,tno from essayinfo where 1=1 " +
            "<if test='mno!=null'>" +
            "and mno=#{mno}" +
            "</if>" +
            " ) e left join essaytype t on e.tno=t.tno  group by e.tno limit 0,5 " +
            "</script>")
    List<ETypeList> FavoriteType(@Param("mno")Integer mno);



}
