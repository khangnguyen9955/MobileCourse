using Android.App;
using Android.OS;
using Android.Widget;
using Android.Content;
using System.Collections.Generic;
using MHike.model;

namespace MHike
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class ShowHikesActivity : Activity
    {
        private ListView hikeListView;
        private Button addHikeButton;
        private EditText inputSearch;

        private LinearLayout linearLayoutDate;
        private LinearLayout linearLayoutLength;

        private List<Hike> hikes;
        private HikeAdapter adapter;
        private HikeDbContext hikeRepository;

        private bool isDateAscending = false;
        private bool isLengthAscending = false;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.feed);

            hikeListView = FindViewById<ListView>(Resource.Id.hikeListView);
            addHikeButton = FindViewById<Button>(Resource.Id.addHikeButton);
            inputSearch = FindViewById<EditText>(Resource.Id.inputSearch);

            linearLayoutDate = FindViewById<LinearLayout>(Resource.Id.linearLayoutDate);
            linearLayoutLength = FindViewById<LinearLayout>(Resource.Id.linearLayoutLength);

            addHikeButton.Click += (sender, e) =>
            {
                Intent intent = new Intent(this, typeof(CreateHikeActivity));
                StartActivity(intent);
            };
                     inputSearch.TextChanged += (sender, e) =>
            {
                FilterHikes(inputSearch.Text);
            };

            linearLayoutDate.Click += (sender, e) =>
            {
                isDateAscending = !isDateAscending;
                TextView dateSort = linearLayoutDate.FindViewById<TextView>(Resource.Id.textViewDate);
                dateSort.Text = isDateAscending ? "Date ▲" : "Date ▼";
                SortHikes("Date"); 
            };

            linearLayoutLength.Click += (sender, e) =>
            {
                isLengthAscending = !isLengthAscending;
                TextView lengthSort = linearLayoutLength.FindViewById<TextView>(Resource.Id.textViewLength);
                lengthSort.Text = isLengthAscending ? "Length ▲" : "Length ▼";
                SortHikes("Length"); 
            };

            LoadHikes();
            hikeListView.ItemClick += (sender, e) =>
            {
                int position = e.Position;
                Hike selectedHike = hikes[position];

                Intent intent = new Intent(this, typeof(GetHikeDetailActivity
                    ));
                intent.PutExtra("HikeId", selectedHike.Id);
                StartActivity(intent);
            };
        }

        private void LoadHikes()
        {
            hikeRepository = new HikeDbContext();
            hikes = new List<Hike>();
            hikes.AddRange(hikeRepository.GetAllHikes());
            adapter = new HikeAdapter(this, hikes);
            hikeListView.Adapter = adapter;
        }

        private void FilterHikes(string query)
        {
            if (string.IsNullOrEmpty(query))
            {
                LoadHikes();
            }
            else
            {
                hikes = hikeRepository.SearchHikes(query);
                adapter = new HikeAdapter(this, hikes);
                hikeListView.Adapter = adapter;
            }
        }

        private void SortHikes(string sortBy)
        {
            if (sortBy == "Date")
            {
                hikes.Sort((hike1, hike2) => hike1.Date.CompareTo(hike2.Date));
            }
            else if (sortBy == "Length")
            {
                hikes.Sort((hike1, hike2) => hike1.Length.CompareTo(hike2.Length));
            }

            if (!isDateAscending || !isLengthAscending)
            {
                hikes.Reverse();
            }

            adapter = new HikeAdapter(this, hikes);
            hikeListView.Adapter = adapter;
        }
    }
}
