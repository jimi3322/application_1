package greendao

import GreenDao.DaoMaster
import GreenDao.UserDao
import GreenDao.UserDao.Properties.Name
import android.content.Context
import com.app.yun.bean.User

class GreenDaoUtils  private constructor(context: Context){
    var myUserDao: UserDao? = null

    init {
        //第一种方法
        /*val devOpenHelper = DaoMaster.DevOpenHelper(context, "my", null)
        val writableDb = devOpenHelper.writableDb
        val daoMaster = DaoMaster(writableDb)
        val newSession = daoMaster.newSession()
        this.myUser = newSession.myUser*/
        //第二种方法
        val newDevSession = DaoMaster.newDevSession(context, "myUser.db")
        myUserDao = newDevSession.userDao
    }
    //伴生  ——   单例
    companion object{
        private var my: GreenDaoUtils? =null
        fun getmy(context: Context): GreenDaoUtils? {
            if(my==null){
                @Synchronized
                if(my==null){
                    my= GreenDaoUtils(context)
                }
            }
            return my
        }
    }


    //添加数据
    fun getinsert(myUser: User){
        myUserDao?.insert(myUser)
    }
    //删除数据
    fun getdelet(myUser: User){
        myUserDao?.delete(myUser)
    }
    //删除数据
    fun getdelet_all(){
        myUserDao?.deleteAll()
    }

    //修改数据
    fun getupdata(myUser: User){
        myUserDao?.update(myUser)
    }
    //查询数据(指定数据)
    fun getquest_count(name:String): MutableList<User>?{
        val where = myUserDao?.queryBuilder()?.where(Name.eq(name))?.list()
        return where
    }
    //查询数据(所有数据)
    fun getquest_true(name:String):MutableList<User>?{
        val list = myUserDao?.queryBuilder()?.list()
        return list
    }
    //查询数据(模糊数据)
    fun getquest_like(name:String):MutableList<User>?{
        val where = myUserDao?.queryBuilder()?.where(Name.like("%$name%"))?.list()
        return where
    }

}