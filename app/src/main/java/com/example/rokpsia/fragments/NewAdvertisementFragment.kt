package com.example.rokpsia.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.rokpsia.R
import com.example.rokpsia.adapter.SpinnerAdapter
import com.example.rokpsia.databinding.FragmentNewAdvertisementBinding
import com.example.rokpsia.models.Pet
import com.example.rokpsia.models.PetBehavior
import com.example.rokpsia.models.PetCharacter
import com.example.rokpsia.models.SpinnerItem
import com.example.rokpsia.utils.Helper
import com.example.rokpsia.viewModel.NewAdvertisementViewModel
import com.example.rokpsia.viewModel.NewAdvertisementViewModelFactory
import www.sanju.motiontoast.MotionToast
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class NewAdvertisementFragment : Fragment() {

    private var ageFromPicker: Int = 0
    private val viewModel: NewAdvertisementViewModel by viewModels { NewAdvertisementViewModelFactory(Helper.provideRepository(requireContext())) }
    private lateinit var bindedView: FragmentNewAdvertisementBinding
    private lateinit var petAdvertisement:Pet
    private var list = mutableListOf<SpinnerItem>()
    private var listbehaviour=mutableListOf<SpinnerItem>()
    private lateinit var adapter: SpinnerAdapter
    private var count=0
    private val requestCode = 101
    private val requestCodeImage = 100

    private val permissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindedView = FragmentNewAdvertisementBinding.inflate(inflater, container, false)

        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Bitmap>("image")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                bindedView.image.setImageBitmap(result)
            }

        if(count==0) {
            initList()
        }

        val items = listOf("Pies", "Kot", "Królik", "Żółw","Chomik","Świnka morska","Fretka")
        val petTypeAdapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (bindedView.dropdownList.editText as? AutoCompleteTextView)?.setAdapter(petTypeAdapter)

        bindedView.agePicker.minValue=0
        bindedView.agePicker.maxValue=100

        bindedView.agePicker.setOnValueChangedListener { picker, oldVal, newVal ->
            ageFromPicker= newVal
        }

        val flag = bundleOf("flag" to true)
        bindedView.planet.setOnClickListener {
            navController.navigate(R.id.action_newAdvertisementFragment_to_registerPart2,flag)
        }

        adapter = SpinnerAdapter(requireContext(), list)
        bindedView.characterSpinner.adapter = adapter
        bindedView.behaviorSpinner.adapter=SpinnerAdapter(requireContext(), listbehaviour)

        bindedView.takePhotoButton.setOnClickListener {
           navController.navigate(R.id.action_newAdvertisementFragment_to_takePhoto)

        }

        bindedView.fromDiskButton.setOnClickListener {
            requestPermissionsIfNecessary()
            val choosePhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(choosePhoto,requestCodeImage)
        }


        bindedView.addAdvertisementButton.setOnClickListener{
            if(viewModel.isFormValid(bindedView)){
                // add new advertisement 
                PreparePetAdvertisement()

                val drawable= bindedView.image.drawable as BitmapDrawable
                val bitmap = drawable.bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val image = stream.toByteArray()

                viewModel.addPetToDatabase(petAdvertisement,image)

                ClearControls()
                ShowToast()
            }
        }

        count++

        return bindedView.root
    }

    private fun ShowToast() {
        viewModel.observeAddingSucces().observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    true-> {
                        MotionToast.createToast(requireActivity(),
                            "Udało się!",
                            "Twój zwierzak został dodany",
                            MotionToast.TOAST_SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))
                    }
                    false -> {
                        MotionToast.createToast(requireActivity(),
                            "Błąd ☹️",
                            "Nie udało się dodać zwierzaka",
                            MotionToast.TOAST_ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))
                    }
                }
            }
        })
    }

    private fun ClearControls() {
        bindedView.descriptionEditText.text.clear()
        bindedView.szczepieniaEditText.text.clear()
        bindedView.nameEditText.text.clear()
        bindedView.healthRatingBar.rating=5f

    }

    private fun PreparePetAdvertisement() {

        val character= PetCharacter(list[0].rate,list[1].rate,list[2].rate,list[3].rate)
        val behavior = PetBehavior(listbehaviour[0].rate,listbehaviour[1].rate,listbehaviour[2].rate,listbehaviour[3].rate)
        val type = bindedView.dropdownList.editText?.text.toString()
        val age=ageFromPicker;

        petAdvertisement = Pet("","",age,type,
        bindedView.nameEditText.text.toString(),
        character,behavior,
        bindedView.healthRatingBar.rating,
        bindedView.descriptionEditText.text.toString(),
        bindedView.szczepieniaEditText.text.toString(),"")

    }


    private fun initList() {
        list.add(SpinnerItem("Spokojny", 4f))
        list.add(SpinnerItem("Płochliwy", 3f))
        list.add(SpinnerItem("Inteligentny", 5f))
        list.add(SpinnerItem("Rodzinny", 2f))

        listbehaviour.add(SpinnerItem("Aktywny", 4f))
        listbehaviour.add(SpinnerItem("Szczekliwy", 3f))
        listbehaviour.add(SpinnerItem("Posłuszny", 5f))
        listbehaviour.add(SpinnerItem("Łakomy", 2f))
    }


    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions and isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(requireContext(), permission) ==
                PackageManager.PERMISSION_GRANTED

    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions.toTypedArray(),
                requestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (this.requestCode == requestCode) {
            requestPermissionsIfNecessary()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestCodeImage) {
                val imageUri: Uri? = data?.clipData?.let {
                    it.getItemAt(0).uri
                } ?: data?.data

                bindedView.image.setImageURI(imageUri)
            }
        }
    }
}


