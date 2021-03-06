package qsos.core.form.view.widget.dialog

import androidx.annotation.DrawableRes
import qsos.core.form.R
import java.util.*

/**
 * @author 华清松
 * @doc 类说明：底部弹窗操作实体
 */
class Operation {

    @DrawableRes
    var iconId = R.drawable.form_dot_black

    var key: String? = null
    var position: Int = 0
    var isCheck: Boolean = false
    var value: Any? = null

    constructor()

    constructor(iconId: Int, key: String, position: Int) {
        this.iconId = iconId
        this.key = key
        this.position = position
    }

    constructor(iconId: Int, key: String, position: Int, value: Any) {
        this.iconId = iconId
        this.key = key
        this.position = position
        this.value = value
    }

    constructor(position: Int, key: String, value: Any) {
        this.key = key
        this.position = position
        this.value = value
    }

    constructor(iconId: Int, key: String, position: Int, check: Boolean, value: Any) {
        this.iconId = iconId
        this.key = key
        this.isCheck = check
        this.position = position
        this.value = value
    }

    companion object {

        fun createOperation(vararg createOperations: Operation): List<Operation> {
            val operations = ArrayList<Operation>()
            if (createOperations.isNotEmpty()) {
                operations.addAll(Arrays.asList(*createOperations))
            }
            return operations
        }
    }
}
