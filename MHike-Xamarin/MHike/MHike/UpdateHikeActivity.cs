using Android.App;
using Android.OS;
using Android.Widget;
using Android.Content;
using System;
using MHike.model;

namespace MHike
{
    [Activity(Label = "Update Hike")]
    public class UpdateHikeActivity : Activity
    {
        private EditText updateHikeNameEditText;
        private EditText updateHikeLocationEditText;
        private TextView updateHikeDateTextView;
        private RadioGroup updateParkingRadioGroup;
        private EditText updateHikeLengthEditText;
        private RadioButton updateEasyRadioButton;
        private RadioButton updateMediumRadioButton;
        private DatePicker updateDatePicker; 
        private RadioButton updateHardRadioButton;
        private EditText updateHikeDescriptionEditText;
        private Button updateHikeButton;
        private Button updateChooseDateButton;
        private DateTime selectedDate;
        private int hikeId;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.update_hike);

            updateHikeNameEditText = FindViewById<EditText>(Resource.Id.updateHikeNameEditText);
            updateHikeLocationEditText = FindViewById<EditText>(Resource.Id.updateHikeLocationEditText);
            updateHikeDateTextView = FindViewById<TextView>(Resource.Id.updateHikeDateTextView);
            updateParkingRadioGroup = FindViewById<RadioGroup>(Resource.Id.updateParkingRadioGroup);
            updateHikeLengthEditText = FindViewById<EditText>(Resource.Id.updateHikeLengthEditText);
            updateEasyRadioButton = FindViewById<RadioButton>(Resource.Id.updateEasyRadioButton);
            updateMediumRadioButton = FindViewById<RadioButton>(Resource.Id.updateMediumRadioButton);
            updateDatePicker = FindViewById<DatePicker>(Resource.Id.updateHikeDatePicker); 
            updateHardRadioButton = FindViewById<RadioButton>(Resource.Id.updateHardRadioButton);
            updateHikeDescriptionEditText = FindViewById<EditText>(Resource.Id.updateHikeDescriptionEditText);
            updateHikeButton = FindViewById<Button>(Resource.Id.updateHikeButton);
            updateChooseDateButton = FindViewById<Button>(Resource.Id.updateChooseDateButton);

            updateChooseDateButton.Click += UpdateChooseDateButton_Click;
            updateHikeButton.Click += UpdateHikeButton_Click;

            hikeId = Intent.GetIntExtra("HikeId", -1);

            HikeDbContext db = new HikeDbContext();
            Hike hike = db.GetHikeById(hikeId);

            if (hike != null)
            {
                updateHikeNameEditText.Text = hike.Name;
                updateHikeLocationEditText.Text = hike.Location;
                updateDatePicker.UpdateDate(hike.Date.Year, hike.Date.Month - 1, hike.Date.Day);
                selectedDate = new DateTime(updateDatePicker.Year, updateDatePicker.Month + 1, updateDatePicker.DayOfMonth);
                updateHikeDateTextView.Text = selectedDate.ToShortDateString();
                updateHikeDateTextView.Visibility = Android.Views.ViewStates.Visible;
                updateParkingRadioGroup.Check(hike.ParkingAvailable ? Resource.Id.updateParkingYesRadioButton : Resource.Id.updateParkingNoRadioButton);
                updateHikeLengthEditText.Text = hike.Length.ToString();
                updateEasyRadioButton.Checked = hike.DifficultyLevel == "Easy";
                updateMediumRadioButton.Checked = hike.DifficultyLevel == "Medium";
                updateHardRadioButton.Checked = hike.DifficultyLevel == "Hard";
                updateHikeDescriptionEditText.Text = hike.Description;
            }
            else
            {
                Toast.MakeText(this, "Hike not found", ToastLength.Short).Show();
            }
        }

        private void UpdateChooseDateButton_Click(object sender, EventArgs e)
        {
            DatePickerDialog datepicker = new DatePickerDialog(this, OnDateSet, selectedDate.Year, selectedDate.Month - 1, selectedDate.Day);
            datepicker.Show();
        }

        private void OnDateSet(object sender, DatePickerDialog.DateSetEventArgs e)
        {
            selectedDate = e.Date;
            updateDatePicker.UpdateDate(selectedDate.Year, selectedDate.Month - 1, selectedDate.Day);
            updateHikeDateTextView.Text = selectedDate.ToShortDateString();
            updateHikeDateTextView.Visibility = Android.Views.ViewStates.Visible;
        }


        private void UpdateHikeButton_Click(object sender, EventArgs e)
        {
            string hikeName = updateHikeNameEditText.Text;
            string hikeLocation = updateHikeLocationEditText.Text;
            int parkingSelected = updateParkingRadioGroup.CheckedRadioButtonId;
            bool parkingAvailable = parkingSelected == Resource.Id.updateParkingYesRadioButton;
            string hikeLengthString = updateHikeLengthEditText.Text;
            string difficultyLevel = GetSelectedDifficultyLevel();
            string description = updateHikeDescriptionEditText.Text;
            HikeDbContext db = new HikeDbContext();

            if (validateInput(hikeName, hikeLocation, difficultyLevel, hikeLengthString))
            {
                float hikeLength = float.Parse(hikeLengthString);
                Hike hike = db.GetHikeById(hikeId);
                hike.Date = selectedDate;
                hike.Name = hikeName;
                hike.Location = hikeLocation;
                hike.ParkingAvailable = parkingAvailable;
                hike.Length = hikeLength;
                hike.DifficultyLevel = difficultyLevel;
                hike.Description = description;

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.SetTitle("Confirm Hike Details");
                alert.SetMessage("Hike Name: " + hikeName + "\nHike Location: " + hikeLocation + "\nDate: " + selectedDate.ToShortDateString() +
                                 "\nParking Available: " + (parkingAvailable ? "Yes" : "No") + "\nLength: " + hikeLength + "\nDifficulty Level: " +
                                 difficultyLevel + "\nDescription: " + description);
                alert.SetPositiveButton("Confirm", (senderAlert, args) =>
                {
                    db.UpdateHikeDetail(hike);
                    Intent editIntent = new Intent(this, typeof(GetHikeDetailActivity));
                    editIntent.PutExtra("HikeId", hikeId);
                    StartActivity(editIntent);
                });
                alert.SetNegativeButton("Cancel", (senderAlert, args) => { });
                Dialog dialog = alert.Create();
                dialog.Show();
            }
        }

        private bool validateInput(string hikeName, string hikeLocation, string difficultyLevel, string hikeLengthString)
        {
            int parkingAvailable = updateParkingRadioGroup.CheckedRadioButtonId;
            bool valid = true;

            if (string.IsNullOrEmpty(hikeName))
            {
                Toast.MakeText(this, "Please enter a hike name.", ToastLength.Short).Show();
                valid = false;
            }

            if (string.IsNullOrEmpty(hikeLocation))
            {
                Toast.MakeText(this, "Please enter a hike location.", ToastLength.Short).Show();
                valid = false;
            }

            if (selectedDate == DateTime.MinValue)
            {
                Toast.MakeText(this, "Please select a hike date.", ToastLength.Short).Show();
                valid = false;
            }

            if (string.IsNullOrEmpty(hikeLengthString))
            {
                Toast.MakeText(this, "Please enter a hike length.", ToastLength.Short).Show();
                valid = false;
            }

            if (parkingAvailable == -1)
            {
                Toast.MakeText(this, "Please select if parking is available.", ToastLength.Short).Show();
                valid = false;
            }

            if (string.IsNullOrEmpty(difficultyLevel))
            {
                Toast.MakeText(this, "Please select a difficulty level.", ToastLength.Short).Show();
                valid = false;
            }

            return valid;
        }

        private string GetSelectedDifficultyLevel()
        {
            if (updateEasyRadioButton.Checked)
            {
                return "Easy";
            }
            else if (updateMediumRadioButton.Checked)
            {
                return "Medium";
            }
            else if (updateHardRadioButton.Checked)
            {
                return "Hard";
            }
            else
            {
                return string.Empty;
            }
        }
    }
}
