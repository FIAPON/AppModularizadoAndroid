package br.com.heiderlopes.modularizacao.feature.listproducts

import androidx.lifecycle.MutableLiveData
import br.com.heiderlopes.domain.entity.Product
import br.com.heiderlopes.domain.usecases.GetProductsUseCase
import br.com.heiderlopes.modularizacao.viewmodel.BaseViewModel
import br.com.heiderlopes.modularizacao.viewmodel.StateMachineSingle
import br.com.heiderlopes.modularizacao.viewmodel.ViewState
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.plusAssign

class ProductListViewModel(
    val useCase: GetProductsUseCase,
    val uiScheduler: Scheduler
): BaseViewModel() {

    val state = MutableLiveData<ViewState<List<Product>>>().apply {
        value = ViewState.Loading
    }

    fun getProducts(forceUpdate: Boolean = false) {
        disposables += useCase.execute(forceUpdate = forceUpdate)
            .compose(StateMachineSingle())
            .observeOn(uiScheduler)
            .subscribe(
                {
                    //onSuccess
                    state.postValue(it)
                },
                {
                    //onError
                }
            )
    }
}
