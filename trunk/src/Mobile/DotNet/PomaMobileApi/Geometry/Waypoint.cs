using System;
using System.Collections.Generic;
using System.Text;

namespace Com.BkitMobile.Poma.Mobile.Api.Geometry
{
    /// <summary>
    /// Contains location's information
    /// </summary>
    public struct Waypoint
    {
        private long _Speed;
        public long Speed
        {
            get
            {
                return _Speed;
            }
            set
            {
                _Speed = value;
            }
        }

        private long _Time;
        public long TimeMilliSeconds
        {
            get
            {
                return _Time;
            }
            set
            {
                _Time = value;
                _DateTime = new DateTime(_Time * 10000L);
            }
        }

        private DateTime _DateTime;
        public DateTime DateTime
        {
            get
            {
                return _DateTime;
            }
            set
            {
                _DateTime = value;
                _Time = _DateTime.Ticks / 10000L;
            }
        }

        private Geocode _Geocode;
        public Geocode Geocode
        {
            get
            {
                return _Geocode;
            }
            set
            {
                _Geocode = Geocode;
            }

        }

        private long _TrackID;
        public long TrackID
        {
            get
            {
                return _TrackID;
            }
            set
            {
                _TrackID = value;
            }
        }

        public Waypoint(Geocode geocode, long speed, long time, long trackID)
        {
            _Speed = speed;
            _Time = time;
            _DateTime = new DateTime(_Time * 10000L);
            _Geocode = geocode;
            _TrackID = trackID;
        }

        public Waypoint(Geocode geocode, long speed, DateTime time, long trackID)
        {
            _Speed = speed;
            _Geocode = geocode;
            _DateTime = time;
            _Time = _DateTime.Ticks / 10000L;
            _TrackID = trackID;
        }

        public override bool Equals(object obj)
        {
            if (!(obj is Waypoint)) return false;
            Waypoint that = (Waypoint)obj;
            return this.Geocode.Equals(that.Geocode) && this._Time == that._Time && this._Speed == that._Speed && this._TrackID == that._TrackID;
        }

        public override int GetHashCode()
        {
            return _Geocode.GetHashCode() + _Speed.GetHashCode() + _Time.GetHashCode() + _TrackID.GetHashCode();
        }

        public override string ToString()
        {
            return string.Format("geo[{0}],spd[{1}],time[{2}],track[{3}]", _Geocode, _Speed, _Time, _TrackID);
        }
    }
}
