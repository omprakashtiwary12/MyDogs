package com.example.mydogs.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.Transition
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

import com.example.mydogs.R
import com.example.mydogs.model.DogBreed
import com.example.mydogs.viewmodel.DetailViewModel
import com.example.util.getProgressDrawable
import com.example.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_dog.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    private lateinit var viewModel: DetailViewModel
    private var mydogUuid = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            mydogUuid = DetailFragmentArgs.fromBundle(it).mydogUuid

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetch(mydogUuid)


        }
        observeViewModel()
    }

    private fun observeViewModel(){
    viewModel.dogLiveData.observe( viewLifecycleOwner, Observer { dog ->
       dog?.let {
           dogName.text = dog.dogBreed
           dogPurpose.text = dog.breedFor
           dogTemperament.text = dog.temperament
           dogLifespan.text = dog.lifeSpan
           context?.let {
               dogImage.loadImage(dog.imageUrl, getProgressDrawable(it))
           }

           dog?.imageUrl?.let {
               setUpBackgroundColor(it)
           }
       }
    })
    }

    private fun setUpBackgroundColor(url: String){
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>(){
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    Palette.from(resource)
                        .generate() {palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            animalLayout.setBackgroundColor(intColor)
                        }
                }

            })
    }
}
