package com.mohaeng.mock

import org.springframework.transaction.support.SimpleTransactionStatus
import org.springframework.transaction.support.TransactionCallback
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created by ShinD on 2022/08/30.
 */
class MockTransactionTemplate : TransactionTemplate() {

    override fun <T> execute(action: TransactionCallback<T>): T? {
        return action.doInTransaction(SimpleTransactionStatus())
    }
}