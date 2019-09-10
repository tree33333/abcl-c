package qsos.core.form.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.form_users.*
import qsos.core.form.FormPath
import qsos.core.form.R
import qsos.core.form.data.FormModelIml
import qsos.core.form.data.FormRepository
import qsos.core.form.db.entity.FormItem
import qsos.core.form.db.entity.FormUserEntity
import qsos.core.form.db.entity.Value
import qsos.core.form.view.adapter.FormUsersAdapter
import qsos.core.form.view.other.FormItemDecoration
import qsos.lib.base.callback.OnTListener
import qsos.lib.base.utils.BaseUtils

/**
 * @author : 华清松
 * 表单用户选择
 */
@Route(group = FormPath.FORM, path = FormPath.FORM_ITEM_USERS)
class UserChoseActivity(
        override var mCompositeDisposable: CompositeDisposable? = CompositeDisposable()
) : AbsFormActivity(), Toolbar.OnMenuItemClickListener {

    /**表单数据实现类*/
    private lateinit var formModelIml: FormModelIml

    @Autowired(name = FormPath.FORM_ITEM_ID)
    @JvmField
    var itemId: Long? = 0

    private var item: FormItem? = null
    private var mList = ArrayList<FormUserEntity>()

    private var chose = 0
    private var limitMin: Int? = 0
    private var limitMax: Int? = 0

    private var mAdapter: FormUsersAdapter? = null
    private var manager: LinearLayoutManager? = null

    override val layoutId: Int = R.layout.form_users
    override val reload: Boolean = false

    override fun initData(savedInstanceState: Bundle?) {
        formModelIml = FormModelIml(FormRepository())
    }

    override fun initView() {
        mAdapter = FormUsersAdapter(mList)
        mAdapter?.setOnChoseListener(object : OnTListener<Int> {
            override fun back(t: Int) {
                chose = t
                changeChoseUser()
            }
        })
        manager = LinearLayoutManager(mContext)
        form_user_rv.layoutManager = manager
        form_user_rv.addItemDecoration(FormItemDecoration())
        form_user_rv.adapter = mAdapter

        form_user_search.setOnClickListener {
            BaseUtils.hideKeyboard(this)
            getData()
        }

        tv_form_user_chose_all.setOnClickListener {
            mAdapter?.changeAllChose(true)
        }
        tv_form_user_chose_cancel.setOnClickListener {
            mAdapter?.changeAllChose(false)
        }

        form_users_et.setOnTouchListener(View.OnTouchListener { _, _ ->
            form_users_et.isFocusable = true
            form_users_et.isFocusableInTouchMode = true
            return@OnTouchListener false
        })

        form_users_et.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                BaseUtils.hideKeyboard(this)
                form_users_et.isFocusable = false
                form_users_et.isFocusableInTouchMode = false
                formModelIml.getUsers(item!!, form_users_et.text.toString())
                return@OnKeyListener true
            }
            return@OnKeyListener false
        })

        getData()

    }

    override fun getData() {
        if (itemId != null) {
            mCompositeDisposable?.add(formModelIml.getFormItemByDB(itemId!!)
                    .subscribe {
                        item = it
                        formModelIml.getUsers(item!!, form_users_et.text.toString())
                                .subscribe { values ->
                                    mList.clear()
                                    values.forEach { v ->
                                        val sFormUserEntity = FormUserEntity()
                                        sFormUserEntity.formItemId = v.formItemId
                                        sFormUserEntity.id = v.id
                                        sFormUserEntity.userName = v.user?.userName
                                        sFormUserEntity.userPhone = v.user?.userDesc
                                        sFormUserEntity.userAvatar = v.user?.userAvatar
                                        sFormUserEntity.userLimit = v.limitEdit
                                        mList.add(sFormUserEntity)
                                    }

                                    chose = getValues().size
                                    limitMin = item?.formItemValue?.limitMin
                                    limitMax = item?.formItemValue?.limitMax

                                    changeChoseUser()

                                    mAdapter?.setLimit(chose, limitMin, limitMax)
                                    mAdapter?.notifyDataSetChanged()
                                }
                    }
            )
        }
    }

    private fun getValues(): List<Value> {
        return item?.formItemValue?.values!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("确认")?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        finish()
        return true
    }

    @SuppressLint("SetTextI18n")
    fun changeChoseUser() {
        if (limitMax != 1) {
            ll_form_user_chose.visibility = View.VISIBLE
            tv_form_user_chose_all.visibility = if (limitMax == -1) View.VISIBLE else View.GONE
            val limitMaxUser = if (limitMax == null || limitMax == -1) "可选人数不限" else "可选 $limitMax 人"
            tv_form_user_chose_num.text = "已选 $chose 人，$limitMaxUser"
        } else {
            ll_form_user_chose.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    override fun dispose() {
        mCompositeDisposable?.dispose()
    }

}
